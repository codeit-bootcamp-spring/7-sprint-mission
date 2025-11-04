package com.sprint.mission.discodeit.repository.JCF;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.InterfaceChannelRepository;

import java.util.*;

//@Repository
public class JCFChannelRepository implements InterfaceChannelRepository { //InterfaceChannelRepository {
    private final Map<UUID, Channel> data;

    public JCFChannelRepository()  {
        this.data = new HashMap<>();
    }

    @Override
    public void save(Channel channel) {
        this.data.put(channel.getId(), channel);
    }

    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.of(this.data.get(id));
    }

    @Override
    public Optional<List<Channel>> findAll() {
        List<Channel> list = this.data.values().stream().toList();
        return Optional.of(list);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public boolean existsByName(String name) {

        return false;
    }
}
