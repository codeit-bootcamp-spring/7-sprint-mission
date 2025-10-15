package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data = new LinkedHashMap<>();

    @Override
    public Message save(Message entity) {
        data.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Message findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public List<Message> findAll() {
        return List.copyOf(data.values());
    }

    @Override
    public boolean deleteById(UUID uuid) {
        return data.remove(uuid) != null;
    }
}
