package com.sprint.mission.discodeit.global.init;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        boolean existsAdmin = userRepository.existsByRole((Role.ADMIN));
        if (existsAdmin) return;

        User admin = new User(
                "admin",
                "admin@naver.com",
                passwordEncoder.encode("admin1234"),
                null,
                Role.ADMIN
        );

        UserStatus status = new UserStatus(admin);

        userRepository.save(admin);
        userStatusRepository.save(status);
    }
}
