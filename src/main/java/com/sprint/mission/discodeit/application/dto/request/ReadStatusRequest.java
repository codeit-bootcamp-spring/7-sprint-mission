package com.sprint.mission.discodeit.application.dto.request;

import java.util.UUID;

public record ReadStatusRequest(
        UUID userId,
        UUID channelId
) {
}
