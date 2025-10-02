package com.sprint.mission.service;

import com.sprint.mission.entity.User;

import java.util.List;

public interface UserService {
    User getUserById(String id);
    List<User> getAllUsers();
    List<String> getAllUsers();

    boolean isCreatableId(String id);
    boolean validatePasswd(String passwd);
    User signIn(String userId, String passwd, String displayName);
    User login(String id, String passwd);

    void deleteUser(String id);

    void setPasswd(String id, String passwd);
    void setBio(String id, String bio);
    void setOnlineStatus(String id, User.Status status);

    User.Status getOnlineStatus(String id);
    String getDisplayName(String id);

    boolean isOnline(String id);

    String getBio(String userId);
}
