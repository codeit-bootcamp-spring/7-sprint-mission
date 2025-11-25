package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.User;

import java.time.Instant;

public record UserStatusDto(
        String userId,
        Instant lastActiveAt
) {
}
