package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messages = new HashMap<>();

    private static JCFMessageRepository instance;
    private JCFMessageRepository() {}

    public static JCFMessageRepository getInstance()
    {
        if(instance == null){
            instance = new JCFMessageRepository();
        }
        return instance;
    }

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        return messages.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }
}
