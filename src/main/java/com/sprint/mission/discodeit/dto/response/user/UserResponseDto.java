package com.sprint.mission.discodeit.dto.response.user;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        UUID profileId,
        boolean online,
        Instant createdAt,
        Instant updatedAt) {
}
