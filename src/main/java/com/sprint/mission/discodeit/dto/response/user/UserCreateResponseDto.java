package com.sprint.mission.discodeit.dto.response.user;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserCreateResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String password,
        UUID profileId
) {
}
