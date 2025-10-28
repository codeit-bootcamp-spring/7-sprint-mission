package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.channel.request.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePrivateResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePublicResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelFindResponse;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelCreatePrivateResponse createPrivateChannel(ChannelCreatePrivateRequest request);
    ChannelCreatePublicResponse createPublicChannel(ChannelCreatePublicRequest request);

    ChannelFindResponse find(UUID channelId);
    List<ChannelFindResponse> findAll();
    List<ChannelFindResponse> findAllByUserId(UUID userId);
    Channel update(UUID channel,String newChannelName, UUID newBose, List<UUID> newUsers);
    void delete(UUID channelId);


}