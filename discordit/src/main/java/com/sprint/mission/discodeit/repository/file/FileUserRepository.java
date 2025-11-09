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
    private final DataWriter dataWriter;

    public FileUserRepository(DataWriter dataWriter) {
        this.dataWriter = dataWriter;
    }

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
    public Optional<User> find(UUID uuid) {
        return Optional.ofNullable(data.get(uuid));
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return data.values().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<User> findAllByUuids(List<UUID> userUuids) {
        return data.values().stream()
                .filter(u -> userUuids.contains(u.getUuid()))
                .toList();
    }


    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
        write();
    }

    @Override
    public boolean exists(UUID uuid) {
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
        dataWriter.writeUser(data);
    }
}

