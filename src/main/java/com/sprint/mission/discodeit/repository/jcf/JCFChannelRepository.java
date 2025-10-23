package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelRepository = new HashMap<>();

    @Override
    public void save(Channel channel) {
        channelRepository.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channelRepository.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelRepository.values());
    }

    @Override
    public void deleteById(UUID id) {
        channelRepository.remove(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return channelRepository.containsKey(id);
    }


}
