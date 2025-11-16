package com.sprint.mission.discodeit.entity.dto;

import java.time.Instant;

public record Dto_ReadStatusUpdate( //all private final
        Instant newLastReadAt
) {
  public Dto_ReadStatusUpdate(Instant newLastReadAt) {
    this.newLastReadAt = newLastReadAt;
  }
}
