package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.User;
import com.sprint.mission.repository.jcf.JCFUserRepository;
import com.sprint.mission.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private static final JCFUserService instance = new JCFUserService();
    private static final JCFUserRepository repository = JCFUserRepository.getInstance();

    private JCFUserService() {
    }

    public static JCFUserService getInstance(){
        return instance;
    }

    @Override
    public User getById(String id) {
        return repository.findById(id);
    }


    @Override
    public List<String> getAllUsers() {
        return repository.findAll().stream()
                .map(User::getUserId)
                .toList();
    }

    @Override
    public List<String> getOnlineUsers() {
        return repository.findAll().stream()
                .filter(u -> u.getOnlineStatus() != User.Status.OFFLINE)
                .map(User::getUserId)
                .toList();
    }



    @Override
    public void signIn(String userId, String passwd, String displayName) {
        repository.save(new User(userId, passwd, displayName));
    }

    @Override
    public boolean login(String id, String passwd) {
        User user = repository.findById(id);
        if(!user.getPasswd().equals(passwd))
            throw new IllegalArgumentException("아이디와 비밀번호가 일치하지 않습니다.");

        user.setOnlineStatus(User.Status.ONLINE);
        repository.update(user);
        return true;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public void setPasswd(String id, String passwd) {
        getById(id).setPasswd(passwd);
    }

    @Override
    public void setBio(String id, String bio) {
        getById(id).setBio(bio);
    }

    @Override
    public void setOnlineStatus(String id, User.Status status) {
        getById(id).setOnlineStatus(status);
    }

    @Override
    public User.Status getOnlineStatus(String id) {
        return getById(id).getOnlineStatus();
    }

    @Override
    public String getDisplayName(String id) {
        return getById(id).getDisplayName();
    }

    @Override
    public boolean isOnline(String id) {
        return getById(id).getOnlineStatus() == User.Status.ONLINE;
    }

    @Override
    public String getBio(String id) {
        return getById(id).getBio();
    }

    @Override
    public void setDisplayName(String userId, String displayName) {
        User user = repository.findById(userId);
        user.setDisplayName(displayName);
        repository.update(user);
    }
}
