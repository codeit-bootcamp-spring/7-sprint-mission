package com.sprint.mission.discodeit.application.dto.response;

import java.time.Duration;
import java.util.UUID;

public record ReadStatusResponse (
        UUID userId,
        UUID channelId,
        Long timeSinceLastRead
){
}
