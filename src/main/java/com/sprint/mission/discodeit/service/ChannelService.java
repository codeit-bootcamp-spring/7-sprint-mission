package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPrivateChannel(CreatePrivateChannelDto createPrivateChannelDto);
    Channel createPublicChannel(CreatePublicChannelDto createPublicChannelDto);
    ChannelResponseDto find(UUID channelId);
    List<ChannelResponseDto> findAllByUserId(UUID userId);
    ChannelResponseDto updateChannel(UUID id, UpdateChannelDto updateChannelDto);
    void deleteChannel(UUID channelId);

    // 요구사항부터 진행한 뒤 구현해보기
//    Channel addMember(UUID channelId, UUID userId);
//    Channel removeMember(UUID channelId, UUID userId);
}
