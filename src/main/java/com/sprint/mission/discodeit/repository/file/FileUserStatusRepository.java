package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.util.file.FileManager;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path filePath;
    private final Map<UUID, UserStatus> userStatuses;

    public FileUserStatusRepository(@Value("${file.path.userStatusPath}") Path userStatusFilePath) {
        this.filePath = userStatusFilePath;
        FileManager.init(filePath);
        userStatuses = FileManager.readFile(filePath);
    }

    @Override
    public void save(UserStatus userStatus) {
        userStatuses.put(userStatus.getId(), userStatus);
        FileManager.writeFile(filePath, userStatuses);
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        return Optional.ofNullable(userStatuses.get(userStatusId));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatuses.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatuses.values());
    }

    @Override
    public void deleteById(UUID userStatusId) {
        userStatuses.remove(userStatusId);
        FileManager.writeFile(filePath, userStatuses);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return userStatuses.containsKey(userId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        this.findByUserId(userId).ifPresent(userStatus -> userStatuses.remove(userId));
        FileManager.writeFile(filePath, userStatuses);
    }

    @Override
    public boolean existsById(UUID userStatusId) {
        return userStatuses.containsKey(userStatusId);
    }
}
