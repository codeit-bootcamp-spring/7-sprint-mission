package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.*;

public interface UserStatusRepository {

    void save(UserStatus userStatus);

    Optional<UserStatus> findByUserId(UUID userId);

    Optional<UserStatus> findById(UUID userStatusId);

    List<UserStatus> findAll();

    void deleteById(UUID userStatusId);

    boolean existsByUserId(UUID userId);

    void deleteByUserId(UUID userId);

    boolean existsById(UUID userStatusId);
}
