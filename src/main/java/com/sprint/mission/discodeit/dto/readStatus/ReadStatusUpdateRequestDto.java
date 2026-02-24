package com.sprint.mission.discodeit.dto.readStatus;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ReadStatusUpdateRequestDto(
        Instant newLastReadAt,
        Boolean newNotificationEnabled
) {
}
