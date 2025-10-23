package com.sprint.mission.discodeit.binarycontent.infrastructure;

import com.sprint.mission.discodeit.binarycontent.application.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBinaryContentRepository implements BinaryContentRepository {

    private final String FILE_PATH = "data/binarycontents.ser"; // 저장 파일 경로

    private Map<UUID, BinaryContent> load() {
        Path filePath = Path.of(FILE_PATH);
        if (Files.notExists(filePath)) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, BinaryContent>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(Map<UUID, BinaryContent> data) {
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
    public void save(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> store = load();
        UUID key = binaryContent.getId();
        store.put(key, binaryContent);
        saveToFile(store);
    }

    @Override
    public void remove(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> store = load();
        store.remove(binaryContent.getId());
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Map<UUID, BinaryContent> store = load();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<BinaryContent> findAll() {
        Map<UUID, BinaryContent> store = load();
        return List.copyOf(store.values());
    }
}
