package com.sprint.mission.service.basic;

import com.sprint.mission.entity.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.repository.file.FileUserRepository;
import com.sprint.mission.repository.jcf.JCFUserRepository;
import com.sprint.mission.service.UserService;

import java.util.*;

public class BasicUserService implements UserService {

    private static final UserRepository userRepository;
    private static final BasicUserService instance;

    static {
        // 1. Repository 인스턴스 생성
        userRepository = FileUserRepository.getInstance();

        // 2. 초기화
        userRepository.init();

        // 3. Service 인스턴스 생성
        instance = new BasicUserService();
    }

    private BasicUserService() {
        // static 블록에서 이미 초기화 완료
    }

    public static BasicUserService getInstance() {
        return instance;
    }

    @Override
    public User getById(String id) {
        return userRepository.findById(id);
    }


    @Override
    public List<String> getAllUsers() {
        return userRepository.findAll().stream()
                .map(User::getUserId)
                .toList();
    }

    @Override
    public List<String> getOnlineUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getOnlineStatus() != User.Status.OFFLINE)
                .map(User::getUserId)
                .toList();
    }


    @Override
    public void signIn(String userId, String passwd, String displayName) {
        userRepository.save(new User(userId, passwd, displayName));
    }

    @Override
    public boolean login(String id, String passwd) {
        User user = userRepository.findById(id);
        if (!user.getPasswd().equals(passwd))
            throw new IllegalArgumentException("아이디와 비밀번호가 일치하지 않습니다.");

        user.setOnlineStatus(User.Status.ONLINE);
        userRepository.update(user);
        return true;
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
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
        User user = userRepository.findById(userId);
        user.setDisplayName(displayName);
        userRepository.update(user);
    }
}
