package com.sprint.mission.discodeit.dto.userStatus.response;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    Instant lastActiveAt,
    boolean online
) {

}
