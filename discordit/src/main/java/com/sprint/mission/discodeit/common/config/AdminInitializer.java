package com.sprint.mission.discodeit.common.config;

import com.sprint.mission.discodeit.common.enums.Roles;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User(adminUsername, passwordEncoder.encode(adminPassword), adminEmail);
            admin.updateRole(Roles.ADMIN);
            userRepository.save(admin);
        }
    }
}