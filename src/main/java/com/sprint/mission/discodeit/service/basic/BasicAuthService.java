package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.auth.InvalidAuthRequestException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.entity.User;
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
    public void logout(UUID userId) {
        log.debug("Logout Request");
        if(userId == null) {
            log.warn("Logout rejected: Invalid request");
            throw new InvalidAuthRequestException("Logout rejected: Invalid request");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Logout failed: User not found");
                    return new UserNotFoundException(userId);
                });

        userStatusRepository.findByUserId(userId).ifPresent(status -> {
            Instant past = Instant.now().minus(Duration.ofMinutes(10));
            status.setLastActiveAt(past);
        } );

        log.info("로그아웃에 성공하였습니다. userId = {}", userId);
    }
}
