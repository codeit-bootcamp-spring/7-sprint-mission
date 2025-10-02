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
    public List<String> getAllUsers() {
        return data.keySet().stream()
                .sorted()
                .toList();
    }

    @Override
    public List<String> getOnlineUsers() {
        return data.values().stream()
                .filter(u -> u.getOnlineStatus() != User.Status.OFFLINE)
                .map(User::getUserId)
                .toList();
    }

    @Override
    public boolean isCreatableId(String id) {
        if(data.containsKey(id)){
            throw new UserAlreadyExistsException(id);
        }
        return User.validateId(id);
    }

    @Override
    public boolean validatePasswd(String passwd) {
        return User.validatePasswd(passwd);
    }



    @Override
    public User signIn(String userId, String passwd, String displayName) {
        if(data.containsKey(userId))
            throw new UserAlreadyExistsException(userId);
        data.put(userId, new User(userId, passwd, displayName));
        return getUserById(userId);
    }

    @Override
    public User login(String id, String passwd) {
        User user = data.get(id);
        if(!user.login(id, passwd))
            throw new IllegalArgumentException("아이디와 비밀번호가 일치하지 않습니다.");

        user.setOnlineStatus(User.Status.ONLINE);
        return user;
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
    public String getDisplayName(String id) {
        return getUserById(id).getDisplayName();
    }

    @Override
    public boolean isOnline(String id) {
        return getUserById(id).getOnlineStatus() == User.Status.ONLINE;
    }

    @Override
    public String getBio(String id) {
        return getUserById(id).getBio();
    }
}
