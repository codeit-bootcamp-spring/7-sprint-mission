package com.sprint.mission.discodeit.global.event;

import java.util.UUID;

public record MessageCreatedEvent(
        UUID messageId,
        UUID channelId,
        UUID senderId
) {
}
