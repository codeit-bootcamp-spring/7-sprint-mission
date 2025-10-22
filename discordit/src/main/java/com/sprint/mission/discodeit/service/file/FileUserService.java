package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class FileUserService implements UserService {
    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        if(!user.getPasswd().equals(passwd))
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
        User user = getById(id);
        user.setPasswd(passwd);
        userRepository.update(user);
    }

    @Override
    public void setBio(String id, String bio) {
        User user = getById(id);
        user.setBio(bio);
        userRepository.update(user);
    }

    @Override
    public void setOnlineStatus(String id, User.Status status) {
        User user = getById(id);
        user.setOnlineStatus(status);
        userRepository.update(user);
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
