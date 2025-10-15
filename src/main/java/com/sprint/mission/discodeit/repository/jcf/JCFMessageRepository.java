package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }
    @Override
    public Message save(Message message) {
        Objects.requireNonNull(message, "message must not be null");
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID uuid) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        return Optional.ofNullable(data.get(uuid));
    }

    @Override
    public List<Message> findAll() {
        return data.values()
                .stream()
                .filter(m -> !m.isDeleted())
                .toList();
    }

    @Override
    public boolean deleteById(UUID uuid) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        return data.remove(uuid) != null;
    }

    @Override
    public List<Message> findByChannel(UUID channelId) {
        Objects.requireNonNull(channelId, "channelId must not be null");
        return data.values()
                .stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .filter(m -> !m.isDeleted())
                .toList();
    }

    @Override
    public List<Message> findByAuthor(UUID authorId) {
        Objects.requireNonNull(authorId, "authorId must not be null");
        return data.values()
                .stream()
                .filter(m -> m.getAuthorId().equals(authorId))
                .filter(m -> !m.isDeleted())
                .toList();
    }

    @Override
    public List<Message> findByChannelIdAndAuthorId(UUID channelId, UUID authorId) {
        Objects.requireNonNull(channelId, "channelId must not be null");
        Objects.requireNonNull(authorId, "authorId must not be null");
        return data.values()
                .stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .filter(m -> m.getAuthorId().equals(authorId))
                .filter(m -> !m.isDeleted())
                .toList();
    }

    @Override
    public List<Message> searchByKeyword(String keyword) {
        String k = Objects.requireNonNull(keyword, "keyword must not be null")
                .trim().toLowerCase();
        if(k.isEmpty()) { return new ArrayList<>(); }
        return data.values()
                .stream()
                .filter(m -> !m.isDeleted())
                .filter(m -> m.getContent().toLowerCase().contains(k))
                .toList();
    }
}
