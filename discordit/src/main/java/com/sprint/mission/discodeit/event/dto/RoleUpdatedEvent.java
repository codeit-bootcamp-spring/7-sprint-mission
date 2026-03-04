package com.sprint.mission.discodeit.event.dto;

import com.sprint.mission.discodeit.common.enums.Roles;

import java.time.Instant;
import java.util.UUID;

public record RoleUpdatedEvent(
    Instant createdAt,
    UUID userId,
    Roles before,
    Roles after
) {
}
