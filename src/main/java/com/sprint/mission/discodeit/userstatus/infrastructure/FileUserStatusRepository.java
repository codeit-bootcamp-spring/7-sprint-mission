package com.sprint.mission.discodeit.userstatus.infrastructure;

import com.sprint.mission.discodeit.userstatus.application.UserStatusRepository;
import com.sprint.mission.discodeit.userstatus.domain.UserStatus;
import org.springframework.stereotype.Repository;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {

    private final String FILE_PATH = "data/userstatuses.ser"; // 저장 파일 경로

    private Map<UUID, UserStatus> load() {
        Path filePath = Path.of(FILE_PATH);
        if (Files.notExists(filePath)) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, UserStatus>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(Map<UUID, UserStatus> data) {
        Path filePath = Path.of(FILE_PATH);
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(UserStatus userStatus) {
        Map<UUID, UserStatus> store = load();
        UUID key = userStatus.getId();
        store.put(key, userStatus);
        saveToFile(store);
    }

    @Override
    public void remove(UserStatus userStatus) {
        Map<UUID, UserStatus> store = load();
        UUID key = userStatus.getId();
        store.remove(key);
        saveToFile(store);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        Map<UUID, UserStatus> store = load();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        Map<UUID, UserStatus> store = load();
        return store.values().stream().filter(us->us.getUserId().equals(userId)).findAny();
    }

    @Override
    public List<UserStatus> findAll() {
        Map<UUID, UserStatus> store = load();
        return List.copyOf(store.values());
    }
}
