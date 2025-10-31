package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    List<Channel> findAll();
    List<Channel> findAllByUserId(UUID userId);
    Channel findById(UUID id);
    Channel findByName(String name);
    Channel update(UUID id, String name, String description);
    Channel delete(UUID id);
    boolean isMember(UUID userId, UUID channelId);
}
