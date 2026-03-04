package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateCommand(
        UUID id,
        Instant readAt,
        Boolean notificationEnabled
) {

    public static ReadStatusUpdateCommand from(UUID id, ReadStatusUpdateRequestDto requestDto) {
        return new ReadStatusUpdateCommand(id, requestDto.newLastReadAt(), requestDto.newNotificationEnabled());
    }
}
