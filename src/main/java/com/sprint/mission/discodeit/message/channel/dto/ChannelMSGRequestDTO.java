package com.sprint.mission.discodeit.message.channel.dto;

import java.util.UUID;

public record ChannelMSGRequestDTO(
        String message,
        UUID channelId,
        UUID senderId
) {
}
