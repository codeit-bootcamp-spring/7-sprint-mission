package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Primary
public class FileUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void save(User user) {
        data.put(user.getUuid(), user);
        write();
    }

    @Override
    public void update(User user) {
        data.put(user.getUuid(), user);
        write();
    }

    @Override
    public void delete(User user) {
        data.remove(user.getUuid());
        write();
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return Optional.ofNullable(data.get(uuid));
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return data.values().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<User> findAllByUserIds(List<String> userIds) {
        return data.values().stream()
                .filter(u -> userIds.contains(u.getUserId()))
                .toList();
    }

    @Override
    public void deleteByUserId(String userId) {
        data.values().removeIf(u -> u.getUserId().equals(userId));
        write();
    }

    @Override
    public void deleteById(UUID uuid) {
        data.remove(uuid);
        write();
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

    private void write() {
        DataWriter.writeUser(data);
    }
}

