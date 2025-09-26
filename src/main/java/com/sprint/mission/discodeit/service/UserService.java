package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.sprint.mission.discodeit.entity.User;

public interface UserService {
    User create(User user);
    Optional<User> read(UUID id);
    List<User> readAll();
    Optional<User> update(UUID id, User updatedUser);
    boolean delete(UUID id);
}

// ChannelService.java와 MessageService.java도 동일한 CRUD 구조로 작성합니다.