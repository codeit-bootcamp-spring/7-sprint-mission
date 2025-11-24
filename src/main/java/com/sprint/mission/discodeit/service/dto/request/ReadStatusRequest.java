package com.sprint.mission.discodeit.service.dto.request;

import java.util.UUID;

public record ReadStatusRequest(
        String userId,
        String channelId
) {
}
