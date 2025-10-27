package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.global.util.file.FileManager;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.*;

@Repository
public class FileMessageRepository implements MessageRepository {
    private final Path filePath;
    private final Map<UUID, Message> messages;

    public FileMessageRepository(@Value("${file.path.messagePath}")Path messageFilePath) {
        this.filePath = messageFilePath;
        FileManager.init(messageFilePath);
        this.messages = FileManager.readFile(messageFilePath);
    }

    @Override
    public void save(Message message) {
        messages.put(message.getId(), message);
        FileManager.writeFile(filePath, messages);
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
        FileManager.writeFile(filePath, messages);
    }

    @Override
    public boolean existsById(UUID messageId) {
        return messages.containsKey(messageId);
    }
}
