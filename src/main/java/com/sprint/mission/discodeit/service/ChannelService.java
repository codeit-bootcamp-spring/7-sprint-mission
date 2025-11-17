package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.dto.channelDto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPublicChannel(PublicChannelCreateRequest requestDto);
    Channel createPrivateChannel(PrivateChannelCreateRequest requestDto);

    // find
    ChannelDto findChannelById(UUID id);

    // findAll
    List<ChannelDto> findAllByUserId(UUID userId);


    Channel updateChannel(UUID id, PublicChannelUpdateRequest updateDto);

    void deleteChannel(UUID id);

}
