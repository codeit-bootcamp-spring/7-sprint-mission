package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.response.auth.AuthLoginResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
        if (authLoginRequestDto == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        String username = authLoginRequestDto.username() == null ? "" : authLoginRequestDto.username().trim();
        String password = authLoginRequestDto.password() == null ? "" : authLoginRequestDto.password();

        List<User> users = userRepository.findByName(username);
        if(users == null || users.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        User user = users.get(0);

        if(user.getPassword() == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password.");
        }

        userStatusRepository.findByUserId(user.getId())
                .ifPresentOrElse(u -> {
                    u.timeUpdated();
                    userStatusRepository.save(u);
                }, () -> userStatusRepository.save(new UserStatus(user.getId()))
                );

        return UserResponseDto.from(user, true);
    }

    @Override
    public void logout(UUID userId) {
        if(userId == null) {
            throw new IllegalArgumentException("Invalid request");
        }

        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user id."));

        userStatusRepository.findByUserId(userId).ifPresent(status -> {
            Instant past = Instant.now().minus(Duration.ofMinutes(10));
            status.setLastReadAt(past);
            userStatusRepository.save(status);
        } );

    }
}
