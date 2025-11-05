package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findAll().stream()
                .filter(u->u.getUsername().equals(request.getUsername()))
                .findFirst();

        User user= userOpt.orElseThrow(() ->
        new NoSuchElementException("User: '" + request.getUsername() + "' not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        // 로그인 성공 -> 상태 갱신
        userStatusRepository.findByUserId(user.getId())
                .ifPresent(UserStatus::LastUpdateUserState);

        boolean isOnline = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(false);

        // dto 반환
        return UserResponse.from(user, isOnline);
    }
}
