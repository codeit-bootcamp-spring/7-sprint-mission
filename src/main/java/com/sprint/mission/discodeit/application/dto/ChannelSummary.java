package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.config.enums.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record ChannelSummary(
        UUID channelId,
        String channelName,
        ChannelType channelType,
        int unreadMessageCount,
        Instant lastMessageDate,
        boolean isPrivate

) {
    public static ChannelSummary from(ChannelResponseDTO channel, int unreadMessageCount) {
        return new ChannelSummary(
                channel.id(),
                channel.channelName(),
                channel.channelType(),
                unreadMessageCount,
                Instant.now(),
                channel.isPrivate()
        );

    }
}
