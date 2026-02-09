package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto create(UserCreateRequest request, Optional<BinaryContentCreateRequest> profileRequest) {
        String email = request.email();
        String rawPassword = request.password();
        String username = request.username();

        if (email == null || email.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            throw new DiscodeitException(ErrorCode.BAD_REQUEST);
        }

        if (username == null || username.isBlank()) {
            throw new DiscodeitException(ErrorCode.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(email)) {
            throw new DiscodeitException(ErrorCode.DUPLICATE_USER);
        }

        if (userRepository.existsByUsername(username)) {
            throw new DiscodeitException(ErrorCode.DUPLICATE_USER);
        }

        BinaryContent nullableProfile = null;
        if (profileRequest.isPresent()) {
            BinaryContentCreateRequest pr = profileRequest.get();
            byte[] bytes = pr.bytes();
            nullableProfile = new BinaryContent(
                    pr.fileName(),
                    (long) bytes.length,
                    pr.contentType()
            );
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, email, encodedPassword, nullableProfile);

        userRepository.save(user);
        return UserDto.from(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto find(UUID userId) {
        return userRepository.findById(userId)
                .map(UserDto::from)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAllWithProfileAndStatus()
                .stream()
                .map(UserDto::from)
                .toList();
    }

    @Override
    public UserDto update(UUID userId, UserUpdateRequest request, Optional<BinaryContentCreateRequest> profileRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));

        String newUsername = request.newUsername();
        String newEmail = request.newEmail();
        String newPassword = request.newPassword();

        if (newEmail != null && !newEmail.isBlank()) {
            if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
                throw new DiscodeitException(ErrorCode.DUPLICATE_USER);
            }
        } else {
            newEmail = user.getEmail();
        }

        if (newUsername != null && !newUsername.isBlank()) {
            if (!newUsername.equals(user.getUsername()) && userRepository.existsByUsername(newUsername)) {
                throw new DiscodeitException(ErrorCode.DUPLICATE_USER);
            }
        } else {
            newUsername = user.getUsername();
        }

        BinaryContent nullableProfile = user.getProfile();
        if (profileRequest.isPresent()) {
            BinaryContentCreateRequest pr = profileRequest.get();
            byte[] bytes = pr.bytes();
            nullableProfile = new BinaryContent(
                    pr.fileName(),
                    (long) bytes.length,
                    pr.contentType()
            );
        }

        String encodedPassword;
        if (newPassword == null || newPassword.isBlank()) {
            encodedPassword = user.getPassword();
        } else {
            encodedPassword = passwordEncoder.encode(newPassword);
        }

        user.update(newUsername, newEmail, encodedPassword, nullableProfile);
        return UserDto.from(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
