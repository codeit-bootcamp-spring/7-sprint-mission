package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageType;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getType() == MessageType.CHANNEL &&
                        m.getChannel().getId().equals(channelId)).collect(Collectors.toList());
    }

    @Override
    public List<Message> findAllByBetweenUserIds(UUID userId1, UUID userId2) {
        return data.values().stream()
                .filter(m -> (m.getAuthor().getId().equals(userId1)
                        && m.getReceiver().getId().equals(userId2))
                || m.getAuthor().getId().equals(userId2) &&  m.getReceiver().getId().equals(userId1))
                .collect(Collectors.toList());
    }
}
