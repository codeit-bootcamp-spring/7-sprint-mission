package com.sprint.mission.discodeit.entity.dto.readStatusDto;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record ReadStatusUpdateDto(@NonNull UUID userId, @NonNull UUID channelId) {
}
