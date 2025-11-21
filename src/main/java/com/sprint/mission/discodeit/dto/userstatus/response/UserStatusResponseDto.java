package com.sprint.mission.discodeit.dto.userstatus.response;


import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto(
        UUID id,
        UUID userId,
        Instant newLastActiveAt
) { }
