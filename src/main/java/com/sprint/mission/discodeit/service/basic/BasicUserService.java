package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/*
 TODO: 프로필 삭제
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;
    private final BinaryContentService binaryContentService;

    @Transactional
    @Override
    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto,
                                  BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        log.debug("Creating user: username = {}, email = {}, hasProfile = {}",
                userCreateRequestDto.username(), userCreateRequestDto.email(), binaryContentCreateRequestDto != null);

        // 요구사항 - 유저 이름과 이메일은 다른 유저와 같으면 안된다.
        if (userRepository.existsByUsername(userCreateRequestDto.username())) {
            log.warn("Create user rejected: user name already exists. username = {}",
                    userCreateRequestDto.username());
            throw new IllegalArgumentException("존재하는 유저입니다!");
        }

        if (userRepository.existsByEmail(userCreateRequestDto.email())) {
            log.warn("Create user rejected: user email already exists. email = {} ",
                    userCreateRequestDto.email());
            throw new IllegalArgumentException("존재하는 이메일입니다!");
        }

        BinaryContent profile = null;
        if (binaryContentCreateRequestDto != null) {
            log.debug("Creating profile fileName = {}, contentType = {}",
                    binaryContentCreateRequestDto.fileName(), binaryContentCreateRequestDto.contentType());
            BinaryContentResponseDto binaryContentResponseDto =
                    binaryContentService.create(binaryContentCreateRequestDto);
            profile = binaryContentRepository.getReferenceById(binaryContentResponseDto.id());
        }

        User user = new User(
                userCreateRequestDto.username(),
                userCreateRequestDto.password(),
                userCreateRequestDto.email(),
                profile
        );


        User save = userRepository.save(user);

        // 요구사항 - userStatus 같이 생성
        userStatusRepository.save(new UserStatus(user));

        boolean online = true;

        log.info("유저가 생성되었습니다! userId = {}, username = {}", save.getId(), save.getUsername());
        return userMapper.toDto(save, online);
    }

    @Override
    public UserResponseDto get(UUID userId) {
        log.debug("Getting user: userId = {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        boolean online = isOnline(user.getId());
        return userMapper.toDto(user, online);
    }

    @Override
    public List<UserResponseDto> getAll() {
        log.debug("Getting all users");
        return userRepository.findAll()
                .stream()
                .map(user -> userMapper.toDto(user, isOnline(user.getId())))
                .toList();
    }

    @Transactional
    @Override
    public UserResponseDto update(UUID userId, UserUpdateRequestDto userUpdateRequestDto,
                                  BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        log.debug("Updating user: userId = {}, hasProfileUpdate = {}",
                userId,binaryContentCreateRequestDto != null);
        User user = userRepository.findById(Objects.requireNonNull(userId))
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // 유저 이름이 비어있지 않고, 기존 이름과 다르다면 수정!
        if(userUpdateRequestDto.newUsername() != null &&
                !userUpdateRequestDto.newUsername().equals(user.getUsername())){
            if(userRepository.findByUsername(userUpdateRequestDto.newUsername())
                    .stream()
                    .anyMatch(u -> !u.getId().equals(userId))) {
                log.warn("Update user rejected: username already exists. userId = {}, newUsername = {}",
                        userId, userUpdateRequestDto.newUsername());
                throw new IllegalArgumentException("존재하는 유저이름입니다!");
            }
            user.setUsername(userUpdateRequestDto.newUsername());
            log.debug("Updating user: userId {}", userId);
        }

        // 유저 이메일이 비어있지 않고, 기존 이메일과 다르다면 수정!
        if(userUpdateRequestDto.newEmail() != null &&
                !userUpdateRequestDto.newEmail().equals(user.getEmail())){
            if(userRepository.findByEmail(userUpdateRequestDto.newEmail())
                    .filter(u -> !u.getId().equals(userId))
                    .isPresent()) {
                log.warn("Update user rejected: email already exists. userId = {}, newEmail = {}",
                        userId, userUpdateRequestDto.newEmail());
                throw new IllegalArgumentException("존재하는 이메일입니다!");
            }
            user.setEmail(userUpdateRequestDto.newEmail());
            log.debug("Updating user: userId = {}", userId);
        }

        if(userUpdateRequestDto.newPassword() != null) {
            user.setPassword(userUpdateRequestDto.newPassword());
            log.debug("Updating user password: userId = {}", userId);
        }

        if(binaryContentCreateRequestDto != null){
            UUID oldProfileId = (user.getProfile()) != null ? user.getProfile().getId() : null;
            BinaryContentResponseDto binaryContentResponseDto = binaryContentService.create(binaryContentCreateRequestDto);
            BinaryContent newProfile = binaryContentRepository.getReferenceById(binaryContentResponseDto.id());
            user.setProfile(newProfile);

            if(oldProfileId != null) {
                binaryContentService.delete(oldProfileId);
            }
            log.debug("Updating user: userId = {}, oldProfile = {}, newProfile = {}",
                    userId, oldProfileId, newProfile);
        }

        User save = userRepository.save(user);
        boolean online = isOnline(save.getId());

        log.info("유저 정보가 수정되었습니다.");
        return userMapper.toDto(save, online);
    }

    @Transactional
    @Override
    public boolean delete(UUID userid) {
        log.debug("Deleting user: userId = {}", userid);
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userRepository.delete(user);
        log.info("유저가 삭제되었습니다.");
        return true;
    }

    // 이름으로 조회
    @Override
    public List<UserResponseDto> getUsersByName(String username) {
        log.debug("Getting users by name {}", username);
        return userRepository.findByUsername(Objects.requireNonNull(username))
                .stream()
                .map(user -> userMapper.toDto(user, isOnline(user.getId())))
                .toList();
    }

    // 이메일로 조회
    @Override
    public Optional<UserResponseDto> getUsersByEmail(String email) {
        log.debug("Getting users by email {}", email);
        return userRepository.findByEmail(Objects.requireNonNull(email))
                .map(user -> userMapper.toDto(user, isOnline(user.getId())));
    }

    // 로그인
    @Transactional
    @Override
    public void login(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userStatusRepository.findByUserId(Objects.requireNonNull(userId))
                .ifPresentOrElse(u -> {
                    u.timeUpdated();
                    }, () -> userStatusRepository.save(new UserStatus(user)));
    }
    // 로그아웃
    @Override
    public void logout(UUID userId) {
    }

    @Override
    public boolean isOnline(UUID userId) {
        return userStatusRepository.findByUserId(Objects.requireNonNull(userId))
                .map(status -> status.isOnlineNow())
                .orElse(false);
    }
}