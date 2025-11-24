package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

    Optional<UserStatus> findById(UUID id);

    Optional<UserStatus> find(UUID binaryId);

    Optional<UserStatus> findByUserId(UUID userId);

    List<UserStatus> findAll();

    UserStatus save(UserStatus userStatus);


    void deleteByUserId(UUID userId);


}
