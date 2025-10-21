package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.user.domain.User;

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
