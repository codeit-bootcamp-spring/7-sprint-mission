package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;


public interface UserService {
    User create(String username, String email, String password,String UserNickName);
    User find(UUID userId);
    List<User> findAll();
    User update(UUID userId, String newUsername, String newEmail, String newPassword, String UserNickName);
    void delete(UUID userId);
}
