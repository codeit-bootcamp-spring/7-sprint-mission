package com.sprint.mssion.discodeit.service;

import com.sprint.mssion.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String username, String email, String phoneNumber, String pronoun);
    User getUserById(UUID uuid);
    List<User> getAllUsers();
    void updateUser(UUID uuid, String username, String email, String phoneNumber, String pronoun);
    void deleteUser(UUID uuid);
    boolean isExistsUser(UUID userId);

    void addChannelToUser(UUID userId, UUID channelId);
    void removeChannelFromAllUsers(UUID channelId);
}
