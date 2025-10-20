package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService{

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public JCFUserService(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void createUser(String userName, String nickName, String email, String phoneNum, String userId, String password) {
        User newUser = new User(userName, nickName, email, phoneNum, userId, password);
        userRepository.save(newUser);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User getUserByPhone(String phoneNum) {
        return userRepository.findByPhone(phoneNum).orElse(null);
    }

    @Override
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User login(String userId, String password) {
        return userRepository.findByUserId(userId)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    public String getUserNickName(UUID id) {
        return userRepository.findById(id)
                .map(User::getNickName)
                .orElse(null);
    }

    @Override
    public void updateUser(User user) {
        userRepository.findById(user.getId()).ifPresent(existing -> {
            if (user.getUserName() == null) user.setUserName(existing.getUserName());
            if (user.getNickName() == null) user.setNickName(existing.getNickName());
            if (user.getEmail() == null) user.setEmail(existing.getEmail());
            if (user.getPhoneNum() == null) user.setPhoneNum(existing.getPhoneNum());
            if (user.getPassword() == null) user.setPassword(existing.getPassword());

            userRepository.update(user);
        });
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.findById(id).ifPresent(user -> {
            messageRepository.deleteByUser(user);
            userRepository.deleteById(id);
        });
    }
}
