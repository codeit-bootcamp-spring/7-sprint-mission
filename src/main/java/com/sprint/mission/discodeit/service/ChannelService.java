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

    Optional<ChannelResponseDto> findChannelInfoById(UUID id);
    Optional<Channel> findChannelEntityById(UUID id);

    // findAll -> findAllByUserId
    List<ChannelResponseDto> findAllByUserId(UUID userId);

    Optional<ChannelResponseDto> findChannelInfoByChannelName(String channelName);

    Optional<ChannelResponseDto> updateChannelName(ChannelUpdateDto channelUpdateDto);
    Optional<ChannelResponseDto> addMemberToChannel(UUID channelId, UUID userId);
    Optional<ChannelResponseDto> removeMemberFromChannel(UUID channelId, UUID userId);


    boolean deleteChannel(UUID id);

}
