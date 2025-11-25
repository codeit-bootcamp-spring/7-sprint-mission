package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);


    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
