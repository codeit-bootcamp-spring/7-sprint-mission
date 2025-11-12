package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel createPrivateChannel(CreatePrivateChannelDto request);

  Channel createPublicChannel(CreatePublicChannelDto request);

  List<ChannelResponseDto> findAllByUserId(UUID userId);

  Channel updateChannel(UUID id, UpdateChannelDto updateChannelDto);

  void deleteChannel(UUID channelId);
}
