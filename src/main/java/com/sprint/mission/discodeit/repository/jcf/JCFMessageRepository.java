package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageRepository = new HashMap<>();

    @Override
    public void save(Message message) {
        messageRepository.put(message.getId(), message);
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

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.values().stream().filter(message -> message.getChannelId().equals(channelId)).toList();
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        messageRepository.values().removeIf(message -> message.getChannelId().equals(channelId));
    }
}
