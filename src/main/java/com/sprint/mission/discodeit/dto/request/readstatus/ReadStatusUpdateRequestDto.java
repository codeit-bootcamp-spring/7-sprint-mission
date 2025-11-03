package com.sprint.mission.discodeit.dto.request.readstatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequestDto(
        UUID id,
        Instant lastReadAt) {
}
