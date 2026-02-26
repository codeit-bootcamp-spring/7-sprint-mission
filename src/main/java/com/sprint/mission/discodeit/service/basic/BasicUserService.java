package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    private final UserMapper userMapper;

    @CacheEvict(value = "users", allEntries = true)
    @Override
    @Transactional
    public UserResponseDto createUser(CreateUserDto createUserDto, Optional<CreateBinaryContentDto> createBinaryContentDto) {
        if (userRepository.findByUsername(createUserDto.username()).isPresent()) {
            throw UserAlreadyExistsException.byUsername(createUserDto.username());
        }
        if (userRepository.findByEmail(createUserDto.email()).isPresent()) {
            throw UserAlreadyExistsException.byEmail(createUserDto.email());
        }

        BinaryContent profile = createBinaryContentDto
                .map(this::saveBinaryContent)
                .orElse(null);

        String encryptedPassword = passwordEncoder.encode(createUserDto.password());
        User user = new User(createUserDto.username(), createUserDto.email(), encryptedPassword, profile);

        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));

        return userMapper.toResponseDto(user);
    }

    @Cacheable(value = "users")
    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAllWithProfile();

        return users.stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @CacheEvict(value = "users", key = "'all'")
    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.userResponseDto.id")
    public UserResponseDto updateUser(UUID userId, UpdateUserDto updateUserDto,
            Optional<CreateBinaryContentDto> createBinaryContentDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));

        if (userRepository.findByUsername(updateUserDto.newUsername()).isPresent()) {
            throw UserAlreadyExistsException.byUsername(updateUserDto.newUsername());
        }
        if (userRepository.findByEmail(updateUserDto.newEmail()).isPresent()) {
            throw UserAlreadyExistsException.byEmail(updateUserDto.newEmail());
        }

        BinaryContent profile = createBinaryContentDto
                .map(this::saveBinaryContent)
                .orElse(null);

        user.updateUser(updateUserDto.newUsername(),
                passwordEncoder.encode(updateUserDto.newPassword()),
                updateUserDto.newEmail(),
                profile
        );

        return userMapper.toResponseDto(user);
    }

    @CacheEvict(value = "users", key = "'all'")
    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.userResponseDto.id")
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));

        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDto initAdmin(String adminEmail, String adminName, String adminPass) {
        return userRepository.findByEmail(adminEmail)
                .map(user -> {
                    // 만약 해당 계정이 존재하는데 Admin이 아니라면
                    // Admin으로 권한 변경
                    if (!user.getRole().equals(Role.ADMIN)) {
                        user.updateRole(Role.ADMIN);
                    }
                    return userMapper.toResponseDto(user);
                })
                .orElseGet(() -> {
                    CreateUserDto createUserDto = new CreateUserDto(adminName, adminPass, adminEmail);
                    return createUser(createUserDto, Optional.empty());
                });
    }

    private BinaryContent saveBinaryContent(CreateBinaryContentDto createBinaryContentDto) {
        BinaryContent binaryContent = BinaryContent.builder()
                .fileName(createBinaryContentDto.fileName())
                .contentType(createBinaryContentDto.contentType())
                .size(createBinaryContentDto.size())
                .build();

        binaryContentRepository.save(binaryContent);
        eventPublisher.publishEvent(new BinaryContentCreatedEvent(this, binaryContent.getId(), createBinaryContentDto.bytes()));

        return binaryContent;
    }
}
