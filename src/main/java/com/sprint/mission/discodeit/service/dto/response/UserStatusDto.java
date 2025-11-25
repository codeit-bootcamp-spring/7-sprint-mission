package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.User;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
        UUID userId,
        Instant lastActiveAt
) {
}
