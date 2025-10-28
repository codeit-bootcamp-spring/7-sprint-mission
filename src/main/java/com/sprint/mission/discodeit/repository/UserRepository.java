package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    List<User> findAll();
    User findById(UUID id);
    User findByEmail(String email);
    List<User> findByNickname(String nickname);
    User update(UUID userId, String nickname, String password);
    User delete(UUID userId);

}
