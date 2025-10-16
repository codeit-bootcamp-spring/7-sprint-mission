package com.sprint.mission.service;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;

import java.util.List;

public interface UserService {
    User getById(String id);
    List<String> getAllUsers();
    List<String> getOnlineUsers();


    void signIn(String userId, String passwd, String displayName);
    boolean login(String id, String passwd);

    void deleteById(String id);

    void setPasswd(String id, String passwd);
    void setBio(String id, String bio);
    void setOnlineStatus(String id, User.Status status);

    User.Status getOnlineStatus(String id);
    String getDisplayName(String id);

    boolean isOnline(String id);

    String getBio(String userId);

    void setDisplayName(String userId, String change);
}
