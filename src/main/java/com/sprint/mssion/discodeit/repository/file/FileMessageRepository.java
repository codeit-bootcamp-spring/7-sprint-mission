package com.sprint.mssion.discodeit.repository.file;
import com.sprint.mssion.discodeit.entity.Message;
import com.sprint.mssion.discodeit.repository.MessageRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
