package com.sprint.mssion.discodeit.service.jcf;

import com.sprint.mssion.discodeit.entity.User;
import com.sprint.mssion.discodeit.repositroy.UserRepository;
import com.sprint.mssion.discodeit.service.UserService;

import java.util.*;

public class JCFUserservice implements UserService {
    private final UserRepository userRepository;

    public JCFUserservice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String username, String password, String email, String phoneNumbers, String pronoun) {
        User newUser = new User(username, email, phoneNumbers, pronoun);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public User getUserById(UUID userId) { // 단건 검색
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 유저: " + userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID userId, String username, String password, String email, String phoneNumbers, String pronoun) {
        User user = this.getUserById(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumbers(phoneNumbers);
        user.setPronoun(pronoun);
        user.getCommon().touch();
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        if (!this.isExistsUser(userId)) {
            throw new NoSuchElementException("찾을 수 없는 유저: " + userId);
        }
        userRepository.deleteById(userId);

    }

    @Override
    public boolean isExistsUser(UUID userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public void addChannelToUser(UUID userId, UUID channelId) {
        User user = getUserById(userId);
        if (!user.getJoinChannels().contains(channelId)) {
            user.addChannel(channelId);
            userRepository.save(user);
        }
    }

    @Override
    public void removeChannelFromAllUsers(UUID channelId) {
        for (User user : this.getAllUsers()) {
            user.removeChannel(channelId);
            userRepository.save(user);
        }
    }


}
