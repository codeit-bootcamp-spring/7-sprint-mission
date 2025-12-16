package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
        log.debug("Login Request");
        if (authLoginRequestDto == null) {
            log.warn("Auth Login rejected: Invalid request");
            throw new IllegalArgumentException("Invalid request");
        }
        String username = authLoginRequestDto.username() == null ? "" : authLoginRequestDto.username().trim();
        String password = authLoginRequestDto.password() == null ? "" : authLoginRequestDto.password();

        User user = userRepository.findByUsername(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if(user.passwordMatch(password)) {
            log.debug("Checking password for user: username = {}", username);
            throw new IllegalArgumentException("Wrong password.");
        }

        userStatusRepository.findByUserId(user.getId())
                .ifPresentOrElse(u -> {
                    u.timeUpdated();
                }, () -> userStatusRepository.save(new UserStatus(user))
                );

        log.info("로그인에 성공하였습니다.");
        return userMapper.toDto(user, true);
    }

    @Transactional
    @Override
    public void logout(UUID userId) {
        log.debug("Logout Request");
        if(userId == null) {
            log.warn("Logout rejected: Invalid request");
            throw new IllegalArgumentException("Invalid request");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id."));

        userStatusRepository.findByUserId(userId).ifPresent(status -> {
            Instant past = Instant.now().minus(Duration.ofMinutes(10));
            status.setLastActiveAt(past);
        } );

        log.info("로그아웃에 성공하였습니다.");
    }
}
