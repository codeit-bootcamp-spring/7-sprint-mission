package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminName;
    @Value("${app.admin.password}")
    private String adminPassword;
    @Value("${app.admin.email}")
    private String adminEmail;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByUsername(adminName).isEmpty()){
            User admin = User.builder()
                    .username(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
        }
    }
}
