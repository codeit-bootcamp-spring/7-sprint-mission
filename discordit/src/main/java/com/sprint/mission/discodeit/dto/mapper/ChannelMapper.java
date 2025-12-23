package com.sprint.mission.discodeit.dto.mapper;

import com.sprint.mission.discodeit.dto.entity.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class ChannelMapper {
    public static ChannelDto toDto(Channel channel, Instant lastMessageAt) {
        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                channel.getParticipants().stream().map(UserMapper::toDto).toList(),
                lastMessageAt
        );
    }
}
