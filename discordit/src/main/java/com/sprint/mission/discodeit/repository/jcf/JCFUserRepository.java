package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.exceptions.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exceptions.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void save(User user) {
        if (existsByUserId(user.getUserId())) {
            throw new UserAlreadyExistsException(user.getUserId());
        }
        data.put(user.getUuid(), user);
    }

    @Override
    public void update(User user) {
        if (!existsByUserId(user.getUserId())) {
            throw new UserNotFoundException(user.getUserId());
        }
        if (!existsById(user.getUuid())) {
            throw new UserNotFoundException(user.getUuid());
        }
        data.put(user.getUuid(), user);
    }

    @Override
    public void delete(User user) {
        if (!data.containsValue(user)) {
            throw new UserNotFoundException(user);
        }
        data.remove(user.getUuid());
    }

    @Override
    public User findById(UUID uuid) {
        if (!data.containsKey(uuid)) {
            throw new UserNotFoundException(uuid);
        }
        return data.get(uuid);
    }

    @Override
    public User findByUserId(String userId) {
        return data.values().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void deleteByUserId(String userId) {
        if (!existsByUserId(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public void deleteById(UUID uuid) {
        if (!existsById(uuid)) {
            throw new UserNotFoundException(uuid);
        }
        data.remove(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return data.containsKey(uuid);
    }

    @Override
    public boolean existsByUserId(String userId) {
        return data.values().stream()
                .anyMatch(u -> u.getUserId().equals(userId));
    }

    @Override
    public List<User> findAll() {
        return data.values().stream()
                .sorted(Comparator.comparing(User::getUserId))
                .toList();
    }

}
