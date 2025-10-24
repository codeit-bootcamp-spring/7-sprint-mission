package com.sprint.mission.discodeit.server.presentation.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ServerReadStatusDto(
        Instant lastReadAt,
        List<UUID> members,
        UUID recentReadUser
) {
}
