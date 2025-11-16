package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.enums.ChannelScope;

import java.time.Instant;
import java.util.UUID;

public record ChannelResponseV2 (
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        ChannelScope type,
        String name,
        String description

) {
    public static ChannelResponseV2 toDto(Channel channel) {
        return new ChannelResponseV2(
                channel.getUuid(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getScope(),
                channel.getChannelName(),
                channel.getDescription()
        );
    }
}
