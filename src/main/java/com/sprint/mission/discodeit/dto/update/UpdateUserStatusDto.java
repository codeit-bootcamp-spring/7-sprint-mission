package com.sprint.mission.discodeit.dto.update;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusDto (
    Instant newAccessTime,
    UUID userStatusId
) {}
