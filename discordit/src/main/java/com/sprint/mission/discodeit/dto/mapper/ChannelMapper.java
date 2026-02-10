package com.sprint.mission.discodeit.dto.mapper;

import com.sprint.mission.discodeit.dto.entity.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ChannelMapper {
    private final UserMapper userMapper;

    public ChannelDto toDto(Channel channel, Instant lastMessageAt) {
        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                channel.getParticipants().stream().map(userMapper::toDto).toList(),
                lastMessageAt
        );
    }
}
