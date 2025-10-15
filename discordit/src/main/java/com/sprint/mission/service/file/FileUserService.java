package com.sprint.mission.service.file;

import com.sprint.mission.entity.User;
import com.sprint.mission.repository.file.FileUserRepository;
import com.sprint.mission.repository.jcf.JCFUserRepository;
import com.sprint.mission.service.UserService;
import com.sprint.mission.service.jcf.JCFUserService;

import java.util.List;

public class FileUserService implements UserService {
    private static final FileUserService instance = new FileUserService();
    private static final FileUserRepository repository = FileUserRepository.getInstance();

    private FileUserService() {
    }

    public static FileUserService getInstance(){
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
        User user = getById(id);
        user.setPasswd(passwd);
        repository.update(user);
    }

    @Override
    public void setBio(String id, String bio) {
        User user = getById(id);
        user.setBio(bio);
        repository.update(user);
    }

    @Override
    public void setOnlineStatus(String id, User.Status status) {
        User user = getById(id);
        user.setOnlineStatus(status);
        repository.update(user);
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
