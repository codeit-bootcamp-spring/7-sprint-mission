package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository {
    User save(User user);

    void delete(User user);

    Optional<User> findById(String id);

    List<User> findAll();

    Optional<User> findByEmail(String email);


    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void updateProfileId(String id, String profileId);
}
