package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelCreateReq req);
    Channel create(ChannelCreateSecReq req);
    void update(UUID id,
                   ChannelUpdateReq req);
    Channel delete(UUID id);
    List<Channel> findAllByUserId(UUID userId);
    Channel findByName(String name);
    Channel findById(UUID id);
}
