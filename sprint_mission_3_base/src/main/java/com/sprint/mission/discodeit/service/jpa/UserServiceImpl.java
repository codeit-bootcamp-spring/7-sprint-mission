package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Primary
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryRepo;
    private final BinaryContentStorage storage;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto create(UserCreateRequest req, Optional<BinaryContentCreateRequest> profileReq) {

        if (userRepository.existsByUsername(req.username())) {
            throw new UserAlreadyExistsException(req.username());
        }

        if (userRepository.existsByEmail(req.email())) {
            throw new UserAlreadyExistsException(req.email());
        }

        String encodedPassword = passwordEncoder.encode(req.password());

        User user = new User(
                req.username(),
                req.email(),
                encodedPassword,
                null
        );

        profileReq.ifPresent(p -> {
            BinaryContent profile = saveProfileAndReturn(user, p);
            user.setProfile(profile);
        });

        UserStatus status = new UserStatus(user, Instant.now());
        user.setStatus(status);

        userRepository.save(user);

        return UserDto.from(user);
    }


    @Override
    public UserDto update(UUID id,
                          UserUpdateRequest req,
                          Optional<BinaryContentCreateRequest> profileReq) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        BinaryContent newProfile = profileReq
                .map(p -> saveProfileAndReturn(user, p))
                .orElse(null);

        String newPassword = req.newPassword();
        String encodedNewPassword = (newPassword != null) ? passwordEncoder.encode(newPassword) : null;

        user.update(
                req.newUsername(),
                req.newEmail(),
                encodedNewPassword,
                newProfile
        );

        return UserDto.from(user);
    }

    @Override
    public UserDto find(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return UserDto.from(user);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .toList();
    }

    @Override
    public void delete(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.delete(user);
    }

    private BinaryContent saveProfileAndReturn(User user, BinaryContentCreateRequest req) {

        BinaryContent binary = BinaryContent.builder()
                .fileName(req.fileName())
                .contentType(req.contentType())
                .size((long) req.bytes().length)
                .build();

        binaryRepo.save(binary);
        storage.put(binary.getId(), req.bytes());

        return binary;
    }
}
