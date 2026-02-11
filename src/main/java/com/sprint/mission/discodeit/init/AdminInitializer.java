package com.sprint.mission.discodeit.init;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.Role;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if(userRepository.existsByRole(Role.ADMIN)){
            return; // 이미 ADMIN 계정이 있다면 진행안함
        }

        User admin = User.create(
                "admin",
                "admin@discodeit.com",
                passwordEncoder.encode("admin123"),
                null,
                Role.ADMIN
        );

        userRepository.save(admin);
    }
}
