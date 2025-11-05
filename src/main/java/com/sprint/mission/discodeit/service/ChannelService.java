package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelResponseDto createChannel(ChannelRequestDto requestDto);

    ChannelResponseDto findChannelInfoById(UUID id);
    ChannelResponseDto findChannelInfoByChannelName(String channelName);

    // findAll -> findAllByUserId
    List<ChannelResponseDto> findAllByUserId(UUID userId);


    ChannelResponseDto updateChannelName(ChannelUpdateDto channelUpdateDto);
    ChannelResponseDto addMemberToChannel(UUID channelId, UUID userId);
    ChannelResponseDto removeMemberFromChannel(UUID channelId, UUID userId);


    void deleteChannel(UUID id);

}
