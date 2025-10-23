package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {
    private final FileManager<User> fileManager;
    public FileUserRepository(FileManager<User> fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void save(User user) {
        Map<UUID, User> users = fileManager.readFile();
        users.put(user.getCommon().getId(), user);
        fileManager.writeFile(users);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        Map<UUID, User> users = fileManager.readFile();
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAll() {
        Map<UUID, User> users = fileManager.readFile();
        return  new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(UUID userId) {
        Map<UUID, User> users = fileManager.readFile();
        users.remove(userId);
        fileManager.writeFile(users);
    }

    @Override
    public boolean existsById(UUID userId) {
        Map<UUID, User> users = fileManager.readFile();
        return users.containsKey(userId);
    }
}
