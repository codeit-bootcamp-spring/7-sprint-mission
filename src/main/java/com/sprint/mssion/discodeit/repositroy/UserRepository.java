package com.sprint.mssion.discodeit.repositroy;

import com.sprint.mssion.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(UUID userId);

    List<User> findAll();

    void deleteById(UUID userId);

    boolean existsById(UUID userId);

}
