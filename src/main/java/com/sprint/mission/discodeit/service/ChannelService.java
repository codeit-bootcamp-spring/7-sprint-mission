package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelService extends BaseService<Channel>{
    void update(UUID id, ChannelDTO channelDTO);
}
