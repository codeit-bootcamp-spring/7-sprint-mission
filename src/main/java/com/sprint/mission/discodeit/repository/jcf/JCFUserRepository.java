/*
package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Objects.requireNonNull(id, "Id cannot be null");
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public boolean deleteById(UUID id) {
        Objects.requireNonNull(id, "Id cannot be null");
        return data.remove(id) != null;
    }

    @Override
    public List<User> findByName(String username) {
        String n = Objects.requireNonNull(username, "Username cannot be null").trim();
        return data.values()
                .stream()
                .filter(u -> u.getUsername().equals(n))
                .toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String e = Objects.requireNonNull(email, "Email cannot be null")
                .toLowerCase().trim();
        return data.values()
                .stream()
                .filter(u -> u.getEmail().equals(e))
                .findFirst();
    }
}

 */