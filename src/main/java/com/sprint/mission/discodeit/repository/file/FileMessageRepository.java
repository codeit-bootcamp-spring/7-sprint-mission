package com.sprint.mission.discodeit.repository.file;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileMessageRepository implements MessageRepository {
    private final FileManager<Message> fileManager;
    public FileMessageRepository(FileManager<Message> fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void save(Message message) {
        Map<UUID, Message> messages = fileManager.readFile();
        messages.put(message.getCommon().getId(), message);
        fileManager.writeFile(messages);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        Map<UUID, Message> messages = fileManager.readFile();
        return Optional.ofNullable(messages.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        Map<UUID, Message> messages = fileManager.readFile();
        return  new ArrayList<>(messages.values());
    }

    @Override
    public void deleteById(UUID messageId) {
        Map<UUID, Message> messages = fileManager.readFile();
        messages.remove(messageId);
        fileManager.writeFile(messages);
    }

    @Override
    public boolean existsById(UUID messageId) {
        Map<UUID, Message> messages = fileManager.readFile();
        return messages.containsKey(messageId);
    }
}
