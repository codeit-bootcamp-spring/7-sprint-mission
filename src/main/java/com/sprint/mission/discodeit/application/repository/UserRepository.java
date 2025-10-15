package com.sprint.mission.discodeit.application.repository;

import com.sprint.mission.discodeit.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void save(User user);

    void remove(User user);

    Optional<User> findById(UUID id);

    List<User> findAll();

    public Optional<User> findByEmail(String email);
}
