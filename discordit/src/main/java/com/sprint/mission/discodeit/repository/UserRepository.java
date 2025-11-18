package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    void update(User user);

    Optional<User> find(UUID uuid);
    Optional<User> findByUserId(String userId);
    Optional<User> findByDisplayName(String displayName);

    void delete(User user);

    List<User> findAllByUuids(List<UUID> userUuids);

    void delete(UUID uuid);

    boolean exists(UUID uuid);
    boolean existsByUserId(String userId);

    List<User> findAll();
}
