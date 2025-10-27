package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.exceptions.ChannelAlreadyExistsException;
import com.sprint.mission.discodeit.exceptions.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private static final JCFChannelRepository instance = new JCFChannelRepository();
    private static final Map<UUID, Channel> data = new HashMap<>();

    private JCFChannelRepository(){}

    public static JCFChannelRepository getInstance() {
        return instance;
    }


    @Override
    public void save(Channel channel) {
        if(existsById(channel.getUuid()))
            throw new ChannelAlreadyExistsException("채널 업데이트는 update를 사용해주세요.");
        data.put(channel.getUuid(), channel);
    }

    @Override
    public void update(Channel channel) {
        if (!existsById(channel.getUuid())) {
            throw new ChannelNotFoundException(channel.getUuid());
        }
        data.put(channel.getUuid(), channel);
    }


    @Override
    public Channel findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream()
                .sorted(Comparator.comparing(Channel::getDisplayName))
                .toList();
    }

    @Override
    public void deleteById(UUID uuid) {
        if (!existsById(uuid)) {
            throw new ChannelNotFoundException(uuid);
        }
        data.remove(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return data.containsKey(uuid);
    }

    @Override
    public boolean existsByName(String name) {
        return data.values().stream()
                .anyMatch(c -> c.getDisplayName().equals(name));
    }


}
