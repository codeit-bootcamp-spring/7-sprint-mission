package com.sprint.mission.discodeit.message.channel.dto;

import com.sprint.mission.discodeit.message.channel.ChannelMessage;

import java.time.Instant;
import java.util.UUID;

public record ChannelMSGResponseDTO(
        UUID id,
        UUID channelId,
        UUID senderId,
        String message,
        Instant createdAt,
        Instant updatedAt

) {
    public static ChannelMSGResponseDTO from(ChannelMessage channelMessage) {
        return new ChannelMSGResponseDTO(
                channelMessage.getId(),
                channelMessage.getChannelId(),
                channelMessage.getSenderId(),
                channelMessage.getMessage(),
                channelMessage.getCreatedAt(),
                channelMessage.getUpdatedAt()
        );
    }
}
