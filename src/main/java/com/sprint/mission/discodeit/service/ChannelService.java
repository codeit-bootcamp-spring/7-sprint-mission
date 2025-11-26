package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {


    void createChannel(Channel channel);
    ChannelDto createPrivateChannel(ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto);
    ChannelDto createPublicChannel(ChannelPublicCreateRequestDto channelPublicCreateRequestDto);
    ChannelDto patchChannel(ChannelPatchRequestDto dto, UUID channelId);
    ChannelDto readChannel(UUID channelId);
    List<ChannelDto> findAllByUserId(UUID userId);
    void deleteChannel(UUID channelId);
    void resetChannelRepository();
    List<ChannelDto> readAllChannel();


}
