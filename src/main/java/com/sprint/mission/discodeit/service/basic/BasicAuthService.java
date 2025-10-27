package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.response.auth.AuthLoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
        if (authLoginRequestDto == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        String username = authLoginRequestDto.getUsername() == null ? "" : authLoginRequestDto.getUsername().trim();
        String password = authLoginRequestDto.getPassword() == null ? "" : authLoginRequestDto.getPassword();

        List<User> users = userRepository.findByName(username);
        if(users == null || users.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        User user = users.get(0);

        String authPassword = user.getPassword();

        if(authPassword == null || !authPassword.equals(password)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        userStatusRepository.findByUserId(user.getId())
                .ifPresentOrElse(u -> {
                    u.timeUpdated();
                    userStatusRepository.save(u);
                }, () -> userStatusRepository.save(new UserStatus(user.getId()))
                );

        return new AuthLoginResponseDto(
                user.getId(),
                user.getUsername()
        );
    }
}
