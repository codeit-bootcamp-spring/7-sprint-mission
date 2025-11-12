package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.*;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(PublicChannelCreateRequest request);
    Channel create(PrivateChannelCreateRequest request);

    ChannelDto find(UUID channelId);

    List<ChannelDto> findAllByUserId(UUID userId);
    Channel update(UUID channelId,ChannelUpdateRequest request);
    void delete(UUID channelId);


}