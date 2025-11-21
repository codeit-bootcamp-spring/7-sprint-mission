package com.sprint.mission.discodeit.dto.converter;

import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class ChannelDtoConverter {
    public ChannelDto toResponseDto(Channel channel, Instant lastMessageAt) {
        return new ChannelDto(
                channel.getId(),
                channel.getVisibility(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getMemberIds(),
                lastMessageAt
        );
    }

}
