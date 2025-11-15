package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf",
        matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> store = new HashMap<>();

    @Override
    public void save(Message message) {
        UUID key = message.getId();
        store.put(key,message);
    }

    @Override
    public void remove(UUID messageId) {
        store.remove(messageId);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(store.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        return List.copyOf(store.values());
    }
}
