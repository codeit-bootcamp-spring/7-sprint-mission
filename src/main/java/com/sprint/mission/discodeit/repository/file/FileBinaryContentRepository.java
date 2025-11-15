package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.BinaryContent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
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
