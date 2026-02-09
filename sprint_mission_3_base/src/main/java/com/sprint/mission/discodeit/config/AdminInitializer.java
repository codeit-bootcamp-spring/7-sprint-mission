package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.UserRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner adminAccountInitializer() {
        return args -> {
            String adminEmail = "admin@discodeit.com";
            if (userRepository.findByEmail(adminEmail).isPresent()) {
                return;
            }

            User admin = new User(
                    UUID.randomUUID(),
                    "admin",
                    adminEmail,
                    passwordEncoder.encode("admin"),
                    UserRole.ADMIN
            );

            userRepository.save(admin);
        };
    }
}
