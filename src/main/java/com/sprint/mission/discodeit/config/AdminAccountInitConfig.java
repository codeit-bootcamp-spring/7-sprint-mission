package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AdminAccountInitConfig {
    @Value("${discodeit.admin.bootstrap.enabled:false}")
    private boolean bootstrapEnabled;

    @Value("${discodeit.admin.bootstrap.username:admin}")
    private String adminUsername;

    @Value("${discodeit.admin.bootstrap.email:admin@discodeit.com}")
    private String adminEmail;

    @Value("${discodeit.admin.bootstrap.password:}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner initAdminAccountRunner() {
        return args -> initAdminAccountIfAbsent();
    }

    protected void initAdminAccountIfAbsent() {
        if (!bootstrapEnabled) {
            log.info("bootstrap is disabled.");
            return;
        }

        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException("[AdminInit] admin_password is null or blank");
        }

        if (userRepository.existsByRole(UserRole.ADMIN)) {
            log.info("[AdminInit] 이미 유저 권한이 ADMIN입니다.");
            return;
        }
        if (userRepository.existsByUsername(adminUsername)) {
            log.info("[AdminInit] ADMIN  이름이 이미 존재합니다.");
            return;
        }

        String encode = passwordEncoder.encode(adminPassword);

        User admin = new User(
                adminUsername,
                encode,
                adminEmail,
                null,
                UserRole.ADMIN
        );

        User saved = userRepository.save(admin);

        log.warn("[AdminInit] ADMIN 계정이 없어 새로 생성했습니다. admin_username={}, admin_email={}",
                admin.getUsername(), admin.getEmail());
    }
}
