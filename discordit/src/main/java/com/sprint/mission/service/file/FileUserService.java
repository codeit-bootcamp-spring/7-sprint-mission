package com.sprint.mission.service.file;

import com.sprint.mission.entity.User;
import com.sprint.mission.service.UserService;

import java.util.List;

public class FileUserService implements UserService {
    @Override
    public User getUserById(String id) {
        return null;
    }

    @Override
    public List<String> getAllUsers() {
        return List.of();
    }

    @Override
    public List<String> getOnlineUsers() {
        return List.of();
    }

    @Override
    public boolean isCreatableId(String id) {
        return false;
    }

    @Override
    public boolean validatePasswd(String passwd) {
        return false;
    }

    @Override
    public User signIn(String userId, String passwd, String displayName) {
        return null;
    }

    @Override
    public User login(String id, String passwd) {
        return null;
    }

    @Override
    public void deleteUser(String id) {

    }

    @Override
    public void setPasswd(String id, String passwd) {

    }

    @Override
    public void setBio(String id, String bio) {

    }

    @Override
    public void setOnlineStatus(String id, User.Status status) {

    }

    @Override
    public User.Status getOnlineStatus(String id) {
        return null;
    }

    @Override
    public String getDisplayName(String id) {
        return "";
    }

    @Override
    public boolean isOnline(String id) {
        return false;
    }

    @Override
    public String getBio(String userId) {
        return "";
    }

    @Override
    public void setDisplayName(String userId, String change) {

    }
}
