package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    void save(UserStatus userStatus);
    void update(UserStatus userStatus);
    void deleteById(UUID id);

    UserStatus findById(UUID id);
    UserStatus findByUser(User user);

    List<UserStatus> findAll();

    boolean exists(UserStatus userStatus);
    boolean existsById(UUID id);
    boolean existsByUser(User user);
}
