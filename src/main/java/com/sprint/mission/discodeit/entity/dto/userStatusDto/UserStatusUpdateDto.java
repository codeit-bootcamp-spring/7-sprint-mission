package com.sprint.mission.discodeit.entity.dto.userStatusDto;

import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserStatusUpdateDto(@NonNull Instant newLastActiveAt) {
}
