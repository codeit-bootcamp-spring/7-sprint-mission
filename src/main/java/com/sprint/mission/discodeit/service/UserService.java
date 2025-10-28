package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(String email, String nickname, String password);
    List<User> findAll();
    User findByEmail(String email);
    User findByNickname(String nickname);
    User delete(UUID id);
    User update(UUID id, String nickname, String password);
}
