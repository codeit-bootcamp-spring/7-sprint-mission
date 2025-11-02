package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(PublicChannelDto publicChannelDto);

    Channel createChannel(PrivateChannelDto createChannelDto);

    ChannelResponseDto getChannel(UUID channelId);

    List<ChannelResponseDto> getAllChannelByUserId(UUID userId);

    void updateChannel(UpdateChannelDto updateChannelDto);

    void deleteChannel(UUID channelId);
}
