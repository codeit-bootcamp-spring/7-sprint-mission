package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }
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
        return new ArrayList<>(data.values());
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
                .filter(u -> u.getUsername().equals(username))
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

    @Override
    public List<User> findByState(UserState state) {
        Objects.requireNonNull(state, "State cannot be null");
        return data.values()
                .stream()
                .filter( u -> u.getUserState() == state)
                .toList();
    }
}