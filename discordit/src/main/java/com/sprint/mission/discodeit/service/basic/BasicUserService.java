package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.enums.OnlineStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Primary
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

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
                .filter(u -> u.getOnlineStatus() != OnlineStatus.OFFLINE)
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

        user.setOnlineStatus(OnlineStatus.ONLINE);
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
    public void setOnlineStatus(String id, OnlineStatus userStatus) {
        getById(id).setOnlineStatus(userStatus);
    }

    @Override
    public OnlineStatus getOnlineStatus(String id) {
        return getById(id).getOnlineStatus();
    }

    @Override
    public String getDisplayName(String id) {
        return getById(id).getDisplayName();
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
