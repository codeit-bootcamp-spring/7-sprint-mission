package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
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
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
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
            BinaryContent profile = saveProfileAndReturn(p);
            user.setProfile(profile);
        });

        UserStatus status = new UserStatus(user, Instant.now());
        user.setStatus(status);

        userRepository.save(user);

        return UserDto.from(user);
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public UserDto update(UUID id,
                          UserUpdateRequest req,
                          Optional<BinaryContentCreateRequest> profileReq) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        BinaryContent newProfile = profileReq
                .map(this::saveProfileAndReturn)
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
    @Cacheable(cacheNames = "users")
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .toList();
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void delete(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.delete(user);
    }

    private BinaryContent saveProfileAndReturn(BinaryContentCreateRequest req) {

        BinaryContent binary = new BinaryContent(
                req.fileName(),
                (long) req.bytes().length,
                req.contentType()
        );

        binaryRepo.save(binary);
        eventPublisher.publishEvent(new BinaryContentCreatedEvent(binary.getId(), req.bytes()));

        return binary;
    }
}