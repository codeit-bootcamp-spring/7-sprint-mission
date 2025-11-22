package com.sprint.mission.discodeit.dto.userStatusDto;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(UUID id, UUID userId, Instant lastActiveAt) {
}
