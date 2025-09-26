package com.sprint.mission.service;

import com.sprint.mission.entity.User;

import java.util.List;

public interface UserService {
    public User getUserById(String id);
    public List<User> getUsers(String... ids);
    public List<User> getAllUsers();

    public void signIn(User user);

    public void deleteUser(String id);

    public void updatePasswd(String id, String passwd);
    public void updateBio(String id, String bio);
    public void updateOnlineStatus(String id, User.Status status);
    public User.Status getOnlineStatus(String id);

    public boolean isOnline(String id);
}
