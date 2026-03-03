package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.jwt.JwtRegistry;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.AuthService;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentManager;
import com.sprint.mission.discodeit.service.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import com.sprint.mission.discodeit.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentManager binaryContentManager;
    private final ReadStatusRepository readStatusRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtRegistry jwtRegistry;
    private final ApplicationEventPublisher eventPublisher;

    @CacheEvict(cacheNames = "usersAll", allEntries = true)
    public UserDto createUser(UserCreateRequest request, MultipartFile file) {
        log.info("UserService.createUser");
        if (userRepository.existsByEmailOrUsername(request.email(), request.username())) {
            throw new UserAlreadyExistsException( ErrorCode.DUPLICATE_USER,new HashMap<>());
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.email(), encodedPassword, request.username());
        User save = userRepository.save(user);

        if (file != null && !file.isEmpty()) {
            BinaryContent content = binaryContentManager.saveFileAndMeta(file);
            save.updateProfile(content);
        }

        List<ReadStatus> readList = new ArrayList<>();
        channelRepository.findAllByType(ChannelType.PUBLIC)
                .forEach(channel -> {
                    ReadStatus readStatus = new ReadStatus(save, channel, false);
                    readList.add(readStatus);
                });
        readStatusRepository.saveAll(readList);

        return mapper.toDto(save);
    }

    @PreAuthorize("#id == authentication.principal.userDto.id")
    @CacheEvict(cacheNames = "usersAll", allEntries = true)
    public UserDto updateUserInfo(UUID id, UserUpdateRequest updateDto, MultipartFile file) {
        log.info("UserService.updateUserInfo");
        User user = userRepository.findByIdWithBinaryContent(id).orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, new HashMap<>()));

        if (updateDto.newUsername() != null) {
            user.updateUsername(updateDto.newUsername());
        }
        if (updateDto.newPassword() != null) {
            user.updatePassword(passwordEncoder.encode(updateDto.newPassword()));
        }
        if (updateDto.newEmail() != null) {
            user.updateEmail(updateDto.newEmail());
        }

        if (file != null) {
            if(user.getProfile()!=null){
                binaryContentManager.deleteFile(user.getProfile());
            }
            BinaryContent content = binaryContentManager.saveFileAndMeta(file);
            user.updateProfile(content);
        }
        return mapper.toDto(user);
    }

    @PreAuthorize("#id == authentication.principal.userDto.id")
    @CacheEvict(cacheNames = "usersAll", allEntries = true)
    public void deleteUser(UUID id) {
        log.info("UserService.deleteUser");
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND,new HashMap<>()));
        if (user.getProfile()!=null){
            binaryContentManager.deleteFile(user.getProfile());
        }
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "usersAll")
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    @CacheEvict(cacheNames = "usersAll", allEntries = true)
    public UserDto updateRole(RoleUpdateRequest roleUpdateRequest){
        User user = userRepository
                .findById(roleUpdateRequest.userId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, new HashMap<>()));

        Role oldRole = user.getRole();
        user.updateRole(roleUpdateRequest.newRole());
        jwtRegistry.invalidateJwtInformationByUserId(user.getId());

        eventPublisher.publishEvent(new RoleUpdatedEvent(
                user.getId(),
                oldRole,
                roleUpdateRequest.newRole()
        ));
        return mapper.toDto(user);
    }
}
