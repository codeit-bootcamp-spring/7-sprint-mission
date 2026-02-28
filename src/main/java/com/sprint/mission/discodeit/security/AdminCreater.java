package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminCreater implements ApplicationRunner {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAccountProperties loginProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createAdmin();
        createDefUser();
    }

    private void createAdmin() {

        boolean isAdmin = userRepository.existsByRole(Role.ADMIN);
        if (!isAdmin) {

            String encodePassword = passwordEncoder.encode(loginProperties.getUserAdminPassword()); //!! 🛠️

            User newUser = new User(loginProperties.getUserAdmin(),
                loginProperties.getUserAdminEmail(),
                encodePassword,
                null);

            newUser.updateRole(Role.ADMIN);
            userRepository.save(newUser);
        }
    }

    private void createDefUser() {

        String encodePassword = passwordEncoder.encode(loginProperties.getUserDefaultPassword()); //!! 🛠️

        User newUser = new User(loginProperties.getUserDefault(),
            loginProperties.getUserDefaultEmail(),
            encodePassword,
            null);

        newUser.updateRole(Role.USER);
        userRepository.save(newUser);
    }
}
