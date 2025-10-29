package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    List<Channel> findAll();
    Channel findById(UUID id);
    Channel findByName(String name);
    Channel update(UUID id, String name);
    Channel delete(UUID id);
}
