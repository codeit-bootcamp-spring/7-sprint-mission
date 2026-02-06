package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminCreater implements ApplicationRunner {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean isAdmin = userRepository.existsByRole(Role.ADMIN);
        if (!isAdmin) {

            String password = "admin123";
            String encodePassword = passwordEncoder.encode(password); //!! 🛠️

            User newUser = new User("admin123",
                "admin123@mail.com",
                encodePassword,
                null);

            newUser.setRole(Role.ADMIN);
            newUser.initUserStatus();

            userRepository.save(newUser);
        }
    }
}
