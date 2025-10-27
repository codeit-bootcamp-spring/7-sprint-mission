package com.sprint.mission.discodeit.application.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ServerReadStatusDto(
        Instant lastReadAt,
        List<UUID> members,
        UUID recentReadUser
) {
}
