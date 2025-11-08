package com.sprint.mission.discodeit.infrastructure.jcf;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf",
        matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> store = new HashMap<>();

    public JCFMessageRepository() {
        UUID userId = UUID.fromString("32121212-1212-1212-1212-121212343434");
        UUID channelId = UUID.fromString("02121212-1212-1212-1212-121212343434");
        UUID messageId = UUID.fromString("92121212-1212-1212-1212-121212343434");
        Message message = new Message(userId, "생성자를 통해 보낸 메세지입니다.", channelId, null);
        store.put(messageId,message);
    }

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
