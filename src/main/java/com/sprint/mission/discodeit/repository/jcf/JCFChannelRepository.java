package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new ConcurrentHashMap<>();

    @Override
    public Channel save(Channel channel) {
        Objects.requireNonNull(channel, "channel cannot be null");
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean deleteById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        return data.remove(id) != null;
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }
}
