package com.sprint.mission.discodeit.dto.readStatusDto;

import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusCreateRequest(@NonNull UUID userId,
                                      @NonNull UUID channelId,
                                      @NonNull Instant lastReadAt) {
}
