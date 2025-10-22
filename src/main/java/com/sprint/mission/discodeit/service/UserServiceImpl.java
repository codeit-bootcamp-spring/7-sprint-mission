package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("Error: Username '" + user.getUsername() + "' already exists.");
        });
        return userRepository.save(user);
    }

    @Override
    public Optional<User> read(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> update(UUID id, User userWithUpdateInfo) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> userByNewUsername = userRepository.findByUsername(userWithUpdateInfo.getUsername());
        if (userByNewUsername.isPresent() && !userByNewUsername.get().getId().equals(id)) {
            throw new IllegalArgumentException("Error: Username '" + userWithUpdateInfo.getUsername() + "' is already taken.");
        }

        User existingUser = optionalUser.get();
        existingUser.update(userWithUpdateInfo.getUsername(), userWithUpdateInfo.getEmail());
        User updatedUser = userRepository.save(existingUser);

        return Optional.of(updatedUser);
    }

    @Override
    public boolean delete(UUID id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}