package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class MemoryChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> store = new HashMap<>();

    public void save(Channel channel){
        UUID key = channel.getId();
        store.put(key, channel);
    }
    public void remove(Channel channel){
        UUID findChannelId = channel.getId();
        store.remove(findChannelId);
    }

    public Optional<Channel> findById(UUID id){
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Channel> findAll() {

        return List.copyOf(store.values().stream().toList());
    }

}
