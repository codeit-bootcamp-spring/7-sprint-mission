package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    void update(UUID userId, String email, String nickname, String password);
    void delete(UUID userId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsById(UUID id);
}
