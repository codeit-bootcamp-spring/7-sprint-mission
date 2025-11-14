package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.UUID;

public record Res_Channel( //all private final
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        ChannelType type,
        String name,
        String description
) {
    public static Res_Channel from(Channel channel) {
        return new Res_Channel(
            channel.getId(),
            channel.getCreatedAt(),
            channel.getUpdatedAt(),
            channel.getChannelType(),
            channel.getChannelName(),
            channel.getDescription()
        );
    }

}
