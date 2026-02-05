package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.enum_.Role;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  @Transactional
  public void initAdmin() {

    boolean adminExists = userRepository.existsByRole(Role.ADMIN);

    if (!adminExists) {
      User admin = new User(
          "admin",
          "admin@admin.com",
          passwordEncoder.encode("admin0000"),
          null
      );
      admin.updateRole(Role.ADMIN);

      UserStatus status = new UserStatus(admin);
      admin.assignStatus(status);

      userRepository.save(admin);
    }
  }
}
