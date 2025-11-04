package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    void update(User user);

    Optional<User> findById(UUID uuid);
    Optional<User> findByUserId(String userId);

    void delete(User user);
    void deleteByUserId(String id);
    void deleteById(UUID uuid);

    boolean existsById(UUID uuid);
    boolean existsByUserId(String userId);

    List<User> findAll();
}
