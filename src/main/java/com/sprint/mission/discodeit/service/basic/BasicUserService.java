package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.user.InvalidUserRequestException;
import com.sprint.mission.discodeit.common.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.common.security.SessionOnlineChecker;
import com.sprint.mission.discodeit.common.security.jwt.JwtOnlineChecker;
import com.sprint.mission.discodeit.common.security.jwt.JwtRegistry;
import com.sprint.mission.discodeit.dto.request.auth.UserRoleUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;
    private final BinaryContentService binaryContentService;
    private final PasswordEncoder passwordEncoder;
    private final SessionOnlineChecker sessionOnlineChecker;
    private final JwtRegistry jwtRegistry;
    private final JwtOnlineChecker jwtOnlineChecker;

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

        String hashedPassword = passwordEncoder.encode(userCreateRequestDto.password());

        User user = new User(
                userCreateRequestDto.username(),
                hashedPassword,
                userCreateRequestDto.email(),
                profile,
                UserRole.USER
        );


        User save = userRepository.save(user);

        log.info("유저가 생성되었습니다! userId = {}, username = {}", save.getId(), save.getUsername());
        return userMapper.toDto(save, false);
    }

    @Override
    public UserResponseDto get(UUID userId) {
        if (userId == null) {
            throw new InvalidUserRequestException("userId is null");
        }
        log.debug("Getting user: userId = {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        boolean online = jwtOnlineChecker.isOnline(userId);
        return userMapper.toDto(user, online);
    }

    @Override
    public List<UserResponseDto> getAll() {
        log.debug("Getting all users");
        List<User> users = userRepository.findAll();
        Set<UUID> ids = users.stream()
                .map(u -> u.getId())
                .collect(Collectors.toSet());
        Map<UUID, Boolean> onlineMap = jwtOnlineChecker.onlineMap(ids);
        return users.stream()
                .map(user -> userMapper.toDto(user, onlineMap.getOrDefault(user.getId(), false)))
                .toList();
    }

    @PreAuthorize("@authz.isSelf(authentication, #userId)")
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
            user.setPasswordHash(passwordEncoder.encode(userUpdateRequestDto.newPassword()));
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
        boolean online = jwtOnlineChecker.isOnline(user.getId());

        log.info("유저 정보가 수정되었습니다. userId = {}", save.getId());
        return userMapper.toDto(save, online);
    }

    @PreAuthorize("@authz.isSelf(authentication, #userid)")
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
                .map(user -> userMapper.toDto(user, jwtOnlineChecker.isOnline(user.getId())))
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
                .map(user -> userMapper.toDto(user, jwtOnlineChecker.isOnline(user.getId())));
    }

    @PreAuthorize("@authz.isSelf(authentication, #userId)")
    @Override
    public UserResponseDto getMe(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        boolean online = jwtOnlineChecker.isOnline(userId);
        return  userMapper.toDto(user, online);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public UserResponseDto updateRole(UserRoleUpdateRequestDto userRoleUpdateRequestDto) {
        if (userRoleUpdateRequestDto == null) {
            throw new InvalidUserRequestException("userRoleUpdateRequestDto is null");
        }

        if (userRoleUpdateRequestDto.newRole() == null) {
            throw new InvalidUserRequestException("newRole is null");
        }

        if (userRoleUpdateRequestDto.userId() == null) {
            throw new InvalidUserRequestException("userId is null");
        }

        UUID userId = userRoleUpdateRequestDto.userId();
        UserRole newRole = userRoleUpdateRequestDto.newRole();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (user.getRole() == newRole) {
            boolean online = jwtOnlineChecker.isOnline(userId);
            return userMapper.toDto(user, online);
        }

        user.updateRole(newRole);
        User saved = userRepository.save(user);

        jwtRegistry.invalidateJwtInformationByUserId(userId);
        boolean online = jwtOnlineChecker.isOnline(userId);

        log.info("User role updated. userId = {}, newRole = {}",  saved.getId(), newRole);
        return userMapper.toDto(saved, online);
    }
}