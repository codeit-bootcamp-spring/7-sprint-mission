package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePrivateResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePublicResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelFindResponse;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelCreatePrivateResponse createPrivateChannel(ChannelCreateRequest request);
    ChannelCreatePublicResponse createPublicChannel(ChannelCreateRequest request);

    ChannelFindResponse find(UUID channelId);
    List<ChannelFindResponse> findAll();
    List<ChannelFindResponse> findAllByUserId(UUID userId);
    Channel update(ChannelUpdateRequest request);
    void delete(UUID channelId);


}