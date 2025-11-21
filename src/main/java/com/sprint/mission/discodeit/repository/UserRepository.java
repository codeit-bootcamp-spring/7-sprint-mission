package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, String> {

//    Optional<User> findByEmail(String email);
//
//
//    Optional<User> findByUsername(String username);
}
