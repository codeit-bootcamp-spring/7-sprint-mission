package com.sprint.mission.discodeit.security;

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
        createAdmin();
        createDefUser();
    }

    private void createAdmin() {

        boolean isAdmin = userRepository.existsByRole(Role.ADMIN);
        if (!isAdmin) {

            String name = "admin123";
            String encodePassword = passwordEncoder.encode(name); //!! 🛠️

            User newUser = new User(name,
                name + "@mail.com",
                encodePassword,
                null);

            newUser.updateRole(Role.ADMIN);
            userRepository.save(newUser);
        }
    }

    private void createDefUser() {

        String name = "1";
        String encodePassword = passwordEncoder.encode(name); //!! 🛠️

        User newUser = new User(name,
            name + "@mail.com",
            encodePassword,
            null);

        newUser.updateRole(Role.USER);
        userRepository.save(newUser);
    }
}
