package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        String name,
        List<UserDto> participants,
        Instant lastMessageAt
) {
    public static ChannelDto from(Channel channel) {
        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                List.of(),
                Instant.MIN
        );
    }
}
