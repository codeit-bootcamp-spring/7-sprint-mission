package com.sprint.mission.discodeit.dto.userStatusDto;

import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;

@Builder
public record UserStatusUpdateRequest(@NonNull Instant newLastActiveAt) {
}
