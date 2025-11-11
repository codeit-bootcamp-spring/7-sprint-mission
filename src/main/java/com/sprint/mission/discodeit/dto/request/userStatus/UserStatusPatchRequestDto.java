package com.sprint.mission.discodeit.dto.request.userStatus;

import java.time.Instant;

public record UserStatusPatchRequestDto(
        Instant newLastActiveAt
) {
}
