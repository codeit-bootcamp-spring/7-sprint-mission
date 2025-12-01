package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelDto createPublicChannel(PublicChannelCreateRequest requestDto);
    ChannelDto createPrivateChannel(PrivateChannelCreateRequest requestDto);

    // find
    ChannelDto findChannelById(UUID id);

    // findAll
    List<ChannelDto> findAllByUserId(UUID userId);


    ChannelDto updateChannel(UUID id, PublicChannelUpdateRequest updateDto);

    void deleteChannel(UUID id);

}
