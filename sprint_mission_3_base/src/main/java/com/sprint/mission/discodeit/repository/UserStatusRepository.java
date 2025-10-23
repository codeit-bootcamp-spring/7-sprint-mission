package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.*;

import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    Optional<UserStatus> findById(UUID id);
    Optional<UserStatus> findByUserId(UUID userId);
    List<UserStatus> findAll();
    boolean existsByUserId(UUID userId);
    void deleteById(UUID id);
}
