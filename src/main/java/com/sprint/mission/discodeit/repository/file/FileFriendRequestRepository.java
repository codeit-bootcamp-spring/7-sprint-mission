package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.FriendRequest;
import com.sprint.mission.discodeit.application.repository.FriendRequestRepository;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileFriendRequestRepository implements FriendRequestRepository {

    private final String FILE_PATH = "data/FriendShip.ser"; // 저장 파일 경로


    private Map<UUID, FriendRequest> load() {
        Path filePath = Path.of(FILE_PATH);
        try {
            if (Files.notExists(filePath)) {
                return new HashMap<>();
            }
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
                return (Map<UUID, FriendRequest>) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(Map<UUID, FriendRequest> data) {
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
    public void save(FriendRequest entity) {
        Map<UUID, FriendRequest> store = load();
        UUID key = entity.getId();
        store.put(key, entity);
        saveToFile(store);

    }

    @Override
    public void remove(FriendRequest entity) {
        Map<UUID, FriendRequest> store = load();
        UUID key = entity.getId();
        store.remove(key, entity);
        saveToFile(store);
    }

    @Override
    public Optional<FriendRequest> findById(UUID id) {
        Map<UUID, FriendRequest> store = load();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<FriendRequest> findAll() {
        Map<UUID, FriendRequest> store = load();
        return List.copyOf(store.values().stream().toList());
    }

}
