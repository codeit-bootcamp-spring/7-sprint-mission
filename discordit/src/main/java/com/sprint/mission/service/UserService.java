package com.sprint.mission.service;

import com.sprint.mission.entity.User;

import java.util.List;

public interface UserService {
    public User getUserById(String id);
    public List<User> getUsers(String... ids);

    public void signIn(User user);

    public void deleteUser(String id);

    public void updateUserId(String id);
    public void updatePasswd(String id);
    public void updateBio(String id);
    public void updateOnlineStatus(String id);

    public boolean isOnline(String id);
}
