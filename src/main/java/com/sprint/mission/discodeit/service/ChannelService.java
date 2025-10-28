package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelInfoDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    ChannelInfoDto createPublicChannel(PublicChannelCreateRequestDto channelCreateDto);
    ChannelInfoDto createPrivateChannel(PrivateChannelCreateRequestDto channelCreateDto);

    Optional<ChannelInfoDto> findChannelInfoById(UUID id);
    Optional<Channel> findChannelEntityById(UUID id);

    // findAll -> findAllByUserId
    List<ChannelInfoDto> findAllByUserId(UUID userId);

    Optional<ChannelInfoDto> findChannelInfoByChannelName(String channelName);

    Optional<ChannelInfoDto> updateChannelName(ChannelUpdateDto channelUpdateDto);
    Optional<ChannelInfoDto> addMemberToChannel(UUID channelId, UUID userId);
    Optional<ChannelInfoDto> removeMemberFromChannel(UUID channelId, UUID userId);


    boolean deleteChannel(UUID id);

}
