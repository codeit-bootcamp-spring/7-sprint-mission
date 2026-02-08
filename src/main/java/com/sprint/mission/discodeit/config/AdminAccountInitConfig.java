package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AdminAccountInitConfig {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@discodeit.com";
    private static final String ADMIN_PASSWORD = "admin123!";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner initAdminAccountRunner() {
        return args -> initAdminAccountIfAbsent();
    }

    protected void initAdminAccountIfAbsent() {
        if (userRepository.existsByRole(UserRole.ADMIN)) {
            log.info("[AdminInit] ADMIN 계정이 이미 존재합니다.");
            return;
        }

        if (userRepository.existsByUsername(ADMIN_USERNAME)) {
            log.info("[AdminInit] ADMIN  이름이 이미 존재합니다.");
            return;
        }

        String encode = passwordEncoder.encode(ADMIN_PASSWORD);

        User admin = new User(
                ADMIN_USERNAME,
                encode,
                ADMIN_EMAIL,
                null,
                UserRole.ADMIN
        );

        User saved = userRepository.save(admin);

        log.warn("[AdminInit] ADMIN 계정이 없어 새로 생성했습니다. admin_username={}, admin_email={}",
                admin.getUsername(), admin.getEmail());
    }
}
