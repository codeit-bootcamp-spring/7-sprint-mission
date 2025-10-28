package com.sprint.mission.discodeit.entity.status.dto;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record ReadStatusCreateDto(@NonNull UUID userId, @NonNull UUID channelId) {
}
