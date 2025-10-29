package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(UUID managerId, String name);
    Channel update(UUID id,
                   String name);
    Channel delete(UUID id);
    List<Channel> findAll();
    Channel findByName(String name);
}
