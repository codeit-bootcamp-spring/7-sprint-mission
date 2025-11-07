package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messages = new ConcurrentHashMap<>();

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }


    @Override
    public List<Message> findByChannelId(UUID channelId) {
        List<Message> channelIds = new ArrayList<>();
        for (Message message : messages.values()) {
            if (message.getChannelId().equals(channelId)) {
                channelIds.add(message);
            }
        }
        return channelIds;
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        messages.remove(channelId);
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }
}
