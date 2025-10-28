package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.application.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.domain.channel.Channel;

public class ChannelDtoMapper {

    private ChannelDtoMapper() {
    }

    public static ChannelResponseDto channelToResponseDto(Channel channel){
        return new ChannelResponseDto(channel.getChannelName(), channel.getServerId(),channel.getId());
    }
}
