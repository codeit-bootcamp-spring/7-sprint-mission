package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileMessageRepository implements MessageRepository {
    private final FileManager<Message> fileManager;
    private Map<UUID, Message> messages;

    public FileMessageRepository(FileManager<Message> fileManager) {
        this.fileManager = fileManager;
        this.messages = new HashMap<>();
    }

    @Override
    public void save(Message message) {
        messages.put(message.getId(), message);
        fileManager.writeFile(messages);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messages.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void deleteById(UUID messageId) {
        messages.remove(messageId);
        fileManager.writeFile(messages);
    }

    @Override
    public boolean existsById(UUID messageId) {
        return messages.containsKey(messageId);
    }
}
