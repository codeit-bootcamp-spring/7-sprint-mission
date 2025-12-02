package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(String emailOrUsername, String password) {

        User user = userRepository.findByEmail(emailOrUsername)
                .or(() -> userRepository.findByName(emailOrUsername))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }

        return user;
    }
}
