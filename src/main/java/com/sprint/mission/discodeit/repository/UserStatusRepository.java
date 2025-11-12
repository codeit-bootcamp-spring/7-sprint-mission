package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

    Optional<UserStatus> findByUserId(UUID userId);

    Optional<UserStatus> find(UUID binaryId);

    List<UserStatus> findAll();

    UserStatus save(UserStatus userStatus);


    void deleteByUserId(UUID userId);


}
