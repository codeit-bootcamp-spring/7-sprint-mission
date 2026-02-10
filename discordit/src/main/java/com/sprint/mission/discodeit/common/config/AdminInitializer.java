package com.sprint.mission.discodeit.common.config;

import com.sprint.mission.discodeit.common.config.properties.AdminProperties;
import com.sprint.mission.discodeit.common.enums.Roles;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties properties;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByUsername(properties.username()).isEmpty()) {
            User admin = new User(properties.username(), passwordEncoder.encode(properties.password()), properties.email());
            admin.updateRole(Roles.ADMIN);
            userRepository.save(admin);
        }
    }
}