package com.sprint.mission.discodeit.dto.converter;

import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.List;

@UtilityClass
public class ChannelDtoConverter {
    public ChannelDto toResponseDto(Channel channel, List<UserResponseDto> participants, Instant lastMessageAt) {
        return new ChannelDto(
                channel.getId(),
                channel.getChannelType(),
                channel.getChannelName(),
                channel.getDescription(),
                participants,
                lastMessageAt
        );
    }

}
