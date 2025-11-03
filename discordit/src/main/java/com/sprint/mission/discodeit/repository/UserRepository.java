package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    void update(User user);
    void delete(User user);

    User findById(UUID uuid);
    User findByUserId(String userId);

    void deleteByUserId(String id);
    void deleteById(UUID uuid);

    boolean existsById(UUID uuid);

    boolean existsByUserId(String userId);

    List<User> findAll();
}
