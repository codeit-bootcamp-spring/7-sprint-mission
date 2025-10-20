package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel insert(Channel channel);
    Channel update(UUID id,
                   String name);
    Channel delete(UUID id);
    List<Channel> findAll();
    Channel findById(UUID id);
    Channel findByName(String name);
}
