package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new LinkedHashMap<>();

    @Override
    public Channel save(Channel entity) {
        data.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Channel findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public List<Channel> findAll() {
        return List.copyOf(data.values());
    }

    @Override
    public boolean deleteById(UUID uuid) {
        return data.remove(uuid) != null;
    }
}
