package com.sprint.mssion.discodeit.repository.file;

import com.sprint.mssion.discodeit.entity.User;
import com.sprint.mssion.discodeit.repository.UserRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private final Path filePath = Paths.get("src", "main", "resources", "Users.ser");
    private final FileManager<User> fileManager;
    public FileUserRepository() {
        fileManager = new FileManager<>(filePath);
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
