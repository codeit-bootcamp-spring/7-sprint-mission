package com.sprint.mission.discodeit.entity.dto.readStatusDto;

import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;

@Builder
public record ReadStatusUpdateRequest(@NonNull Instant newLastReadAt) {
}
