package com.sprint.mission.discodeit.dto.readStatusDto;

import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;

@Builder
public record ReadStatusUpdateRequest(@NonNull Instant newLastReadAt) {
}
