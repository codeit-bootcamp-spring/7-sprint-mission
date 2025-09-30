package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public interface UserService {
    void createUser(String userName, String nickName, String email, String phoneNum, String userId, String password);
    User getUserByEmail(String email);
    User getUserByPhone(String phoneNum);
    User getUserByUserId(String userId);
    Collection<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(UUID id);
    void removeChannelFromUser(UUID id, User user);
}
