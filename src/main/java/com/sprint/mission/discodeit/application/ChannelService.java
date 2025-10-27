package com.sprint.mission.discodeit.application;


import com.sprint.mission.discodeit.application.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.ChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelResponseDto createChannel(ChannelCreateRequestDto requestDto);

    ChannelResponseDto findById(UUID id);

    List<ChannelResponseDto> findAllByServer(UUID serverId);

    ChannelResponseDto updateChannel(ChannelRequestDto requestDto);

    void deleteAllByServer(UUID serverId);

    void deleteChannel(UUID channelId);

}
