package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void save(User user);

    void remove(User user);

    Optional<User> findById(UUID id);

    List<User> findAll();

    Optional<User> findByEmail(String email);


    Optional<User> findByUsername(String username);
}
