package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.User;
import com.sprint.mission.exceptions.UserAlreadyExistsException;
import com.sprint.mission.exceptions.UserNotFoundException;
import com.sprint.mission.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private static final Map<String, User> data = new HashMap<>();// 유저 id, User객체 (id검색을 빠르게 하기 위함)

    @Override
    public User getUserById(String id) {
        User user = data.get(id);
        if(user == null)
            throw new UserNotFoundException(id);
        return user;
    }

    @Override
    public List<User> getUsers(String... ids) {
        List<User> users = new ArrayList<>();
        for(String id : ids){
            users.add(getUserById(id));
        }
        return users;
    }

    @Override
    public List<User> getAllUsers() {
        return data.values().stream().toList();
    }

    @Override
    public void signIn(String userId, String passwd, String displayName) {
        if(data.containsKey(userId))
            throw new UserAlreadyExistsException(userId);
        data.put(userId, new User(userId, passwd, displayName));
    }

    @Override
    public void deleteUser(String id) {
        data.remove(id);
    }

    @Override
    public void setPasswd(String id, String passwd) {
        getUserById(id).setPasswd(passwd);
    }

    @Override
    public void setBio(String id, String bio) {
        getUserById(id).setBio(bio);
    }

    @Override
    public void setOnlineStatus(String id, User.Status status) {
        getUserById(id).setOnlineStatus(status);
    }

    @Override
    public User.Status getOnlineStatus(String id) {
        return getUserById(id).getOnlineStatus();
    }

    @Override
    public boolean isOnline(String id) {
        return getUserById(id).getOnlineStatus() == User.Status.ONLINE;
    }
}
