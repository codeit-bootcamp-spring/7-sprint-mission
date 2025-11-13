package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.channelDto.PublicChannelRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PublicChannelUpdateDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PrivateChannelRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPublicChannel(PublicChannelRequestDto requestDto);
    Channel createPrivateChannel(PrivateChannelRequestDto requestDto);

    // find
    ChannelResponseDto findChannelById(UUID id);

    // findAll
    List<ChannelResponseDto> findAllByUserId(UUID userId);


    Channel updateChannel(UUID id, PublicChannelUpdateDto updateDto);

    void deleteChannel(UUID id);

}
