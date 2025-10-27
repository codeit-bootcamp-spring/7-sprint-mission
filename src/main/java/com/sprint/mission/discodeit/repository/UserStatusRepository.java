package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.*;

public interface UserStatusRepository {

    void save(UserStatus userStatus);

    Optional<UserStatus> findById(UUID userStatusId);

    List<UserStatus> findAll();

    void deleteById(UUID userStatusId);

    boolean existsById(UUID userStatusId);
}
