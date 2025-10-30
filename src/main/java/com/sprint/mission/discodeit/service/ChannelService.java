package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelCreateReq req);
    Channel create(ChannelCreateSecReq req);
    Channel update(UUID id,
                   String name);
    Channel delete(UUID id);
    List<Channel> findAll();
    Channel findByName(String name);
}
