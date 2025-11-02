package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.util.file.FileManager;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserRepository implements UserRepository {
    private final Path filePath;
    private final Map<UUID, User> users;

    public FileUserRepository(@Value("${file.path.userPath}") Path userFilePath) {
        this.filePath = userFilePath;
        FileManager.init(filePath);
        this.users = FileManager.readFile(filePath);
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
        FileManager.writeFile(filePath, users);
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
        FileManager.writeFile(filePath, users);
    }

    @Override
    public boolean existsById(UUID userId) {
        return users.containsKey(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
