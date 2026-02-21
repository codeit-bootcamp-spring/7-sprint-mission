package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record MessageCreatedEvent(
        UUID channelId,
        String channelName,
        String authorName,
        String content
) {

}
