package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(User user);
    Optional<User> read(UUID id);
    List<User> readAll();
    Optional<User> update(UUID id, User userWithUpdateInfo);
    boolean delete(UUID id);
}