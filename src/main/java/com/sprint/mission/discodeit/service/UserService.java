package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import com.sprint.mission.discodeit.service.dto.response.UserStatusDto;
import com.sprint.mission.discodeit.service.mapper.UserMapper;
import com.sprint.mission.discodeit.service.mapper.UserStatusMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentService binaryContentService;
    private final ReadStatusRepository readStatusRepository;
    private final UserMapper mapper;
    private final UserStatusMapper userStatusMapper;

    @Transactional
    public UserDto createUser(UserCreateRequest request, MultipartFile file) {
        log.info("UserService.createUser");
        if (userRepository.existsByEmail(request.email())) {
            throw new UserException(Instant.now(), ErrorCode.USER_NOT_FOUND, new HashMap<>());
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new UserException(Instant.now(), ErrorCode.USER_NOT_FOUND, new HashMap<>());
        }

        User user = new User(request.email(), request.password(), request.username());


        User save = userRepository.save(user);


        if (file != null && !file.isEmpty()) {
            BinaryContent content = binaryContentService.put(save.getId(), file);
            save.updateProfile(content);
        }

        List<ReadStatus> readList = new ArrayList<>();
        channelRepository.findAllByType(ChannelType.PUBLIC)
                .forEach(channel -> {
                    ReadStatus readStatus = new ReadStatus(save, channel, Instant.now());
                    readList.add(readStatus);
                });
        readStatusRepository.saveAll(readList);

        return mapper.toDto(save);
    }

    @Transactional
    public UserDto updateUserInfo(UUID id, UserUpdateRequest updateDto, MultipartFile file) {
        log.info("UserService.updateUserInfo");
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));

        if (updateDto.newUsername() != null) {
            user.updateUsername(updateDto.newUsername());
        }
        if (updateDto.newPassword() != null) {
            user.updatePassword(updateDto.newPassword());
        }
        if (updateDto.newEmail() != null) {
            user.updateEmail(updateDto.newEmail());
        }

        if (file != null) {
            BinaryContent content = binaryContentService.put(user.getId(), file);
            user.updateProfile(content);
            //여기서 업데이트하면 알아서 전 profile은 삭제 쿼리 날라감
        }
        return mapper.toDto(user);
    }


    @Transactional
    public void deleteUser(UUID id) {
        log.info("UserService.deleteUser");
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        userRepository.delete(user);
        binaryContentService.deleteFile(user.getId());
    }


    @Transactional
    public List<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    public UserDto login(String loginId, String password) {

        User user = userRepository
                .findByUsername(loginId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다");
        }

        return mapper.toDto(user);

    }

    @Transactional
    public UserStatusDto updateLastActiveAt(UUID id, Instant lastActiveAt) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));

        user.updateActiveAt(lastActiveAt);
        return userStatusMapper.toDto(user.getUserStatus());
    }


}
