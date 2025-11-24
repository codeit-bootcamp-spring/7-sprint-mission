package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


public record Res_ChannelFind( //all private final
       //@NotBlank(message = "channelId is mandatory")
        UUID id,
        ChannelType type,
        String name,
        String description,
        List<UUID> participantIds,
        Instant lastMessageAt
) {
    public static Res_ChannelFind from(ChannelDto channel, Instant lastMessageAt, List<UUID> privateChannelUserIDs) {
        return new Res_ChannelFind(
            channel.id(),
            channel.channelType(),
            channel.channelName(),
            channel.description(),
            privateChannelUserIDs,
            lastMessageAt
        );
    }
}
