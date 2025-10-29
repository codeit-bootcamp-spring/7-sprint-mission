package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Primary
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> store = new ConcurrentHashMap<>();
    // channelId -> messageIds
    private final Map<UUID, Set<UUID>> byChannel = new ConcurrentHashMap<>();

    @Override
    public Message save(Message m) {
        store.put(m.getId(), m);
        byChannel.computeIfAbsent(m.getChannelId(), k -> new HashSet<>()).add(m.getId());
        return m;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        Set<UUID> ids = byChannel.getOrDefault(channelId, Collections.emptySet());
        return ids.stream()
                .map(store::get)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Message::getCreatedAt)) // 필요하면 정렬
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        Message removed = store.remove(id);
        if (removed == null) return;
        Set<UUID> s = byChannel.get(removed.getChannelId());
        if (s != null) {
            s.remove(id);
            if (s.isEmpty()) byChannel.remove(removed.getChannelId());
        }
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        Set<UUID> ids = byChannel.remove(channelId);
        if (ids == null) return;
        for (UUID id : ids) {
            store.remove(id);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }
}
