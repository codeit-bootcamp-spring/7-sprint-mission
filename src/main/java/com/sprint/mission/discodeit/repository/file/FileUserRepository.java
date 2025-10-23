package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {
    private final FileManager<User> fileManager;
    private final Map<UUID, User> users;

    public FileUserRepository(FileManager<User> fileManager) {
        this.fileManager = fileManager;
        this.users = new HashMap<>();
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
        fileManager.writeFile(users);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(UUID userId) {
        users.remove(userId);
        fileManager.writeFile(users);
    }

    @Override
    public boolean existsById(UUID userId) {
        return users.containsKey(userId);
    }
}
