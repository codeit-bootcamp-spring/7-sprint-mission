package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelInfoDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    ChannelInfoDto createChannel(ChannelCreateRequestDto channelCreateDto);

    Optional<ChannelInfoDto> findChannelInfoById(UUID id);
    Optional<Channel> findChannelEntityById(UUID id);
    List<ChannelInfoDto> findAll();
    Optional<ChannelInfoDto> findChannelInfoByChannelName(String channelName);

    Optional<ChannelInfoDto> updateChannelName(UUID channelId, String channelName);
    Optional<ChannelInfoDto> addMemberToChannel(UUID channelId, UUID userId);
    Optional<ChannelInfoDto> removeMemberFromChannel(UUID channelId, UUID userId);

    boolean deleteChannel(UUID id);

}
