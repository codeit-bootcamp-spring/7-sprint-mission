package com.sprint.mission.discodeit.dto.request.readStatus;

import java.time.Instant;

public record ReadStatusPatchRequestDto(
        Instant newLastReadAt
) {
}
