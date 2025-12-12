package com.sprint.mission.discodeit.dto.readStatusDto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusCreateRequest(@NotNull UUID userId,
                                      @NotNull UUID channelId,
                                      @NotNull Instant lastReadAt) {
}
