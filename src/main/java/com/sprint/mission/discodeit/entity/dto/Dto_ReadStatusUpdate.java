package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record Dto_ReadStatusUpdate( //all private final
        @NotNull
        Instant newLastReadAt
) {
  public Dto_ReadStatusUpdate(Instant newLastReadAt) {
    this.newLastReadAt = newLastReadAt;
  }
}
