package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.user.InvalidUserRequestException;
import com.sprint.mission.discodeit.common.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
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
        if (userCreateRequestDto == null) {
            throw new InvalidUserRequestException("userCreateRequestDto is null");
        }
        log.debug("Creating user: username = {}, email = {}",
                userCreateRequestDto.username(), userCreateRequestDto.email());

        // 요구사항 - 유저 이름과 이메일은 다른 유저와 같으면 안된다.
        if (userRepository.existsByUsername(userCreateRequestDto.username())) {
            log.warn("Create user rejected: user name already exists. username = {}",
                    userCreateRequestDto.username());
            throw UserAlreadyExistsException.byUsername(userCreateRequestDto.username());
        }

        if (userRepository.existsByEmail(userCreateRequestDto.email())) {
            log.warn("Create user rejected: user email already exists. email = {} ",
                    userCreateRequestDto.email());
            throw UserAlreadyExistsException.byEmail(userCreateRequestDto.email());
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
        userStatusRepository.save(new UserStatus(save));

        boolean online = true;

        log.info("유저가 생성되었습니다! userId = {}, username = {}", save.getId(), save.getUsername());
        return userMapper.toDto(save, online);
    }

    @Override
    public UserResponseDto get(UUID userId) {
        if (userId == null) {
            throw new InvalidUserRequestException("userId is null");
        }
        log.debug("Getting user: userId = {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
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
        if (userUpdateRequestDto == null) {
            throw new InvalidUserRequestException("userUpdateRequestDto is null");
        }
        if (userId == null) {
            throw new InvalidUserRequestException("userId is null");
        }
        log.debug("Updating user: userId = {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        // 유저 이름이 비어있지 않고, 기존 이름과 다르다면 수정!
        if(userUpdateRequestDto.newUsername() != null &&
                !userUpdateRequestDto.newUsername().equals(user.getUsername())){
            if(userRepository.findByUsername(userUpdateRequestDto.newUsername())
                    .stream()
                    .anyMatch(u -> !u.getId().equals(userId))) {
                log.warn("Update user rejected: username already exists. userId = {}, newUsername = {}",
                        userId, userUpdateRequestDto.newUsername());
                throw UserAlreadyExistsException.byUsername(userUpdateRequestDto.newUsername());
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
                throw UserAlreadyExistsException.byEmail(userUpdateRequestDto.newEmail());
            }
            user.setEmail(userUpdateRequestDto.newEmail());
            log.debug("Updating user: userId = {}", userId);
        }

        if(userUpdateRequestDto.newPassword() != null) {
            user.setPassword(userUpdateRequestDto.newPassword());
            log.debug("Updating user password: userId = {}", userId);
        }

        if (binaryContentCreateRequestDto != null) {
            UUID oldProfileId = (user.getProfile()) != null ? user.getProfile().getId() : null;
            BinaryContentResponseDto binaryContentResponseDto = binaryContentService.create(binaryContentCreateRequestDto);
            BinaryContent newProfile = binaryContentRepository.getReferenceById(binaryContentResponseDto.id());
            user.setProfile(newProfile);

            if (oldProfileId != null) {
                binaryContentService.delete(oldProfileId);
            }
            log.debug("Updating user: userId = {}, oldProfile = {}, newProfile = {}",
                    userId, oldProfileId, newProfile);
        }

        User save = userRepository.save(user);
        boolean online = isOnline(save.getId());

        log.info("유저 정보가 수정되었습니다. userId = {}", save.getId());
        return userMapper.toDto(save, online);
    }

    @Transactional
    @Override
    public boolean delete(UUID userid) {
        if (userid == null) {
            throw new InvalidUserRequestException("userid is null");
        }
        log.debug("Deleting user: userId = {}", userid);
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UserNotFoundException(userid));

        userRepository.delete(user);
        log.info("유저가 삭제되었습니다. userId = {}", userid);
        return true;
    }

    // 이름으로 조회
    @Override
    public List<UserResponseDto> getUsersByName(String username) {
        if (username == null) {
            throw new InvalidUserRequestException("username is null");
        }
        log.debug("Getting users by name {}", username);
        return userRepository.findByUsername(username)
                .stream()
                .map(user -> userMapper.toDto(user, isOnline(user.getId())))
                .toList();
    }

    // 이메일로 조회
    @Override
    public Optional<UserResponseDto> getUsersByEmail(String email) {
        if (email == null) {
            throw new InvalidUserRequestException("email is null");
        }
        log.debug("Getting users by email {}", email);
        return userRepository.findByEmail(email)
                .map(user -> userMapper.toDto(user, isOnline(user.getId())));
    }

    // 로그인
    @Transactional
    @Override
    public void login(UUID userId) {
        if (userId == null) {
            throw new InvalidUserRequestException("userId is null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userStatusRepository.findByUserId(userId)
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
        if (userId == null) {
            throw new InvalidUserRequestException("userId is null");
        }
        return userStatusRepository.findByUserId(userId)
                .map(status -> status.isOnlineNow())
                .orElse(false);
    }
}