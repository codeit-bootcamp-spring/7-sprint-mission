package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint. mission.discodeit.exception. user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com. sprint.mission.discodeit. repository.BinaryContentRepository;
import com. sprint.mission.discodeit. repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time. Instant;
import java.util. List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok. extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest,
                          Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        log.debug("사용자 생성 시도: username={}, email={}", username, email);

        if (userRepository.existsByEmail(email)) {
            log.warn("사용자 생성 실패 - 중복 이메일: {}", email);
            throw new UserAlreadyExistsException("email", email);
        }
        if (userRepository.existsByUsername(username)) {
            log.warn("사용자 생성 실패 - 중복 사용자명: {}", username);
            throw new UserAlreadyExistsException("username", username);
        }

        BinaryContent nullableProfile = optionalProfileCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                            contentType);
                    binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(binaryContent.getId(), bytes);
                    log.debug("프로필 이미지 저장 완료: id={}", binaryContent.getId());
                    return binaryContent;
                })
                .orElse(null);
        String password = userCreateRequest.password();

        User user = new User(username, email, password, nullableProfile);
        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(user, now);

        userRepository.save(user);
        log.info("사용자 생성 완료: id={}, username={}", user.getId(), username);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto find(UUID userId) {
        log.debug("사용자 조회:  id={}", userId);
        return userRepository.findById(userId)
                .map(userMapper:: toDto)
                .orElseThrow(() -> {
                    log.warn("사용자 조회 실패 - 존재하지 않음: id={}", userId);
                    return new UserNotFoundException(userId);
                });
    }

    @Override
    public List<UserDto> findAll() {
        log.debug("전체 사용자 조회");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
                          Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        log.debug("사용자 수정 시도: id={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("사용자 수정 실패 - 존재하지 않음: id={}", userId);
                    return new UserNotFoundException(userId);
                });

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();

        if (newEmail != null && !newEmail.equals(user.getEmail()) && userRepository. existsByEmail(newEmail)) {
            log.warn("사용자 수정 실패 - 중복 이메일: {}", newEmail);
            throw new UserAlreadyExistsException("email", newEmail);
        }
        if (newUsername != null && !newUsername.equals(user. getUsername()) && userRepository.existsByUsername(newUsername)) {
            log.warn("사용자 수정 실패 - 중복 사용자명: {}", newUsername);
            throw new UserAlreadyExistsException("username", newUsername);
        }

        BinaryContent nullableProfile = optionalProfileCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                            contentType);
                    binaryContentRepository. save(binaryContent);
                    binaryContentStorage.put(binaryContent.getId(), bytes);
                    log.debug("프로필 이미지 업데이트 완료: id={}", binaryContent.getId());
                    return binaryContent;
                })
                .orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        user.update(newUsername, newEmail, newPassword, nullableProfile);

        log.info("사용자 수정 완료: id={}", userId);
        return userMapper. toDto(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        log.debug("사용자 삭제 시도: id={}", userId);

        if (!userRepository.existsById(userId)) {
            log.warn("사용자 삭제 실패 - 존재하지 않음: id={}", userId);
            throw new UserNotFoundException(userId);
        }

        userRepository.deleteById(userId);
        log.info("사용자 삭제 완료: id={}", userId);
    }
}
