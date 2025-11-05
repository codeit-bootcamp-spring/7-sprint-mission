package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.config.enums.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record ChannelSummaryDTO(
        UUID channelId,
        String channelName,
        ChannelType channelType,
        int unreadMessageCount,
        Instant lastMessageDate,
        boolean isPrivate

) {
    public static ChannelSummaryDTO from(ChannelResponseDTO channel,int unreadMessageCount) {
        return new ChannelSummaryDTO(
                channel.id(),
                channel.channelName(),
                channel.channelType(),
                unreadMessageCount,
                Instant.now(),
                channel.isPrivate()
        );

    }
}
