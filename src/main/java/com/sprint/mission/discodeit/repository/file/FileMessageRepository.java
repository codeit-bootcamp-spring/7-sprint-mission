package com.sprint.mission.discodeit.repository.file;


import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {

    private final String FILE_PATH = "data/Message.ser"; // 저장 파일 경로


    private Map<UUID, Message> load() {
        Path filePath = Path.of(FILE_PATH);

        if (Files.notExists(filePath)) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


    private void saveToFile(Map<UUID, Message> data) {
        Path filePath = Path.of(FILE_PATH);

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Message message) {
        Map<UUID, Message> store = load();
        UUID key = message.getId();
        store.put(key, message);
        saveToFile(store);
    }

    @Override
    public void remove(UUID messageId) {
        Map<UUID, Message> store = load();
        store.remove(messageId);
        saveToFile(store);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        Map<UUID, Message> store = load();
        return Optional.ofNullable(store.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        Map<UUID, Message> store = load();
        return List.copyOf(store.values());
    }
}
