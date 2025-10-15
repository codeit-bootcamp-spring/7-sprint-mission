package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.FriendShip;
import com.sprint.mission.discodeit.service.repository.FriendShipRepository;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JCFriendShipRepository implements FriendShipRepository {

    private final String FILE_PATH = "data/FriendShip.ser"; // 저장 파일 경로


    private Map<UUID, FriendShip> load() {
        Path filePath = Path.of(FILE_PATH);
        try {
            if (Files.notExists(filePath)) {
                return new HashMap<>();
            }
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
                return (Map<UUID, FriendShip>) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(Map<UUID, FriendShip> data) {
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
    public void save(FriendShip friendShip) {
        Map<UUID, FriendShip> store = load();
        UUID key = friendShip.getId();
        store.put(key, friendShip);
        saveToFile(store);
    }

    @Override
    public void remove(FriendShip friendShip) {
        Map<UUID, FriendShip> store = load();
        UUID key = friendShip.getId();
        store.remove(key, friendShip);
        saveToFile(store);
    }

    @Override
    public Optional<FriendShip> findById(UUID id) {
        Map<UUID, FriendShip> store = load();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<FriendShip> findAll() {
        Map<UUID, FriendShip> store = load();
        return List.copyOf(store.values().stream().toList());
    }
}
