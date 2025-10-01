package com.sprint.mssion.discodeit.repositroy.jcf;

import com.sprint.mssion.discodeit.entity.Message;
import com.sprint.mssion.discodeit.repositroy.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageRepository = new HashMap<>();

    @Override
    public void save(Message message) {
        messageRepository.put(message.getCommon().getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageRepository.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageRepository.values());
    }

    @Override
    public void deleteById(UUID messageId) {
        messageRepository.remove(messageId);
    }
    @Override
    public boolean existsById(UUID messageId) {
        return messageRepository.containsKey(messageId);
    }
}
