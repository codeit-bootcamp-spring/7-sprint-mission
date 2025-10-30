package com.sprint.mission.discodeit.dto.request.userstatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequestDto(
        UUID id,
        Instant lastReadAt) {
}
