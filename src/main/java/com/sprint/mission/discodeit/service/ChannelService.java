package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelResponseDto createChannel(CreatePublicChannelDto createPublicChannelDto);

  ChannelResponseDto createChannel(CreatePrivateChannelDto createChannelDto);

  ChannelResponseDto getChannel(UUID channelId);

  List<ChannelResponseDto> getAllChannelByUserId(UUID userId);

  ChannelResponseDto updateChannel(UUID channelId, UpdateChannelDto updateChannelDto);

  void deleteChannel(UUID channelId);
}
