package com.sprint.mission.discodeit.dto.readStatusDto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ReadStatusUpdateRequest(
        Instant newLastReadAt,
        Boolean newNotificationEnabled
) {
}
