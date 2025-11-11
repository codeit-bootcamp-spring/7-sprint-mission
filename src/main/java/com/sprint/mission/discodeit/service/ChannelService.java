package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.response.*;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPrivateChannel(ChannelCreateRequest request);
    Channel createPublicChannel(ChannelCreateRequest request);

    ChannelDto find(UUID channelId);
    List<ChannelFindResponse> findAll();
    List<ChannelFindResponse> findAllByUserId(UUID userId);
    ChannelUpdateResponse update(ChannelUpdateRequest request);
    void delete(UUID channelId);


}