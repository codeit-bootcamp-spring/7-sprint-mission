package com.sprint.mission.discodeit.entity.dto.readStatusDto;

import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusUpdateDto(@NonNull Instant newLastReadAt) {
}
