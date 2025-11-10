package com.sprint.mission.discodeit.application.dto.request;

import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID userId,
        UUID channelId
) {
}
