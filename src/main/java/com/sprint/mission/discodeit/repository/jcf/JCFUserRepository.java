package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true
)
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> userRepo;


    public JCFUserRepository() {
        this.userRepo = new HashMap<>();
        resetUserRepository();
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return Optional.ofNullable(userRepo.get(userId));
    }

    @Override
    public Optional<User> getUserByName(String userName) {
        return userRepo.values().stream().filter(x -> x.getName().equals(userName)).findFirst();
    }

    @Override
    public Optional<User> getUser(User user) {
        return getUserById(user.getId());
    }

    @Override
    public List<User> getAllUser() {
        return userRepo.values().stream().toList();
    }

    @Override
    public User saveUser(User user) {
        userRepo.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepo.remove(userId);
    }

    @Override
    public void updateUser(User user) {
        deleteUser(user.getId());
        saveUser(user);

    }

    @Override
    public List<User> getUpdatedUser() {
        return userRepo.values().stream().filter(x -> x.getUpdatedAt() != x.getCreatedAt()).toList();
    }


    @Override
    public void resetUserRepository() {
        userRepo.clear();

    }

    @Override
    public boolean isUserExit(UUID userId) {
        return userRepo.containsKey(userId);
    }


}
