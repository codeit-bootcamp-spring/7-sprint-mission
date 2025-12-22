package com.sprint.mission.discodeit.dto.response.login;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public record LoginResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String password,
        UUID profileId
) {

}
