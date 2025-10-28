package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.user.UserRepository;
import com.sprint.mission.discodeit.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public final class LoginService {
    private final UserRepository userRepository;

    public User login(String loginId, String password) {
        return userRepository.findByUsername(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
