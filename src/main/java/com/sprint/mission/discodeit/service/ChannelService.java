package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PrivateChannelRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PublicChannelRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    ChannelResponseDto createPublicChannel(PublicChannelRequestDto requestDto);
    ChannelResponseDto createPrivateChannel(PrivateChannelRequestDto requestDto);

    ChannelResponseDto findChannelInfoById(UUID id);
    ChannelResponseDto findChannelInfoByChannelName(String channelName);

    // findAll -> findAllByUserId
    List<ChannelResponseDto> findAllByUserId(UUID userId);


    ChannelResponseDto updateChannelName(ChannelUpdateDto channelUpdateDto);
    ChannelResponseDto addMemberToChannel(UUID channelId, UUID userId);
    ChannelResponseDto removeMemberFromChannel(UUID channelId, UUID userId);


    void deleteChannel(UUID id);

}
