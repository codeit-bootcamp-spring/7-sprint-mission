package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        String adminEmail = "admin@discodeit.com";

        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = new User(
                "admin",
                adminEmail,
                passwordEncoder.encode("admin1234"),
                null
        );

        admin.setRole(UserRole.ADMIN);

        userRepository.save(admin);
    }
}
