package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String username, String email, String phoneNumber, String pronoun) {
        User newUser = new User(username, email, phoneNumber, pronoun);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public User getUser(UUID userId) { // 단건 검색
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 유저: " + userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID userId, String username, String email, String phoneNumber, String pronoun) {
        User user = this.getUser(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
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
        User user = getUser(userId);
        if (!user.getJoinChannels().contains(channelId)) {
            user.addChannel(channelId);
            user.getCommon().touch();
            userRepository.save(user);
        }
    }

    @Override
    public void removeChannelFromAllUsers(UUID channelId) {
        for (User user : this.getAllUsers()) {
            user.removeChannel(channelId);
            user.getCommon().touch();
            userRepository.save(user);
        }
    }


}
