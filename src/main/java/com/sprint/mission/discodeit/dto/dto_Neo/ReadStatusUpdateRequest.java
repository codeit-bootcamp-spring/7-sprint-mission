package com.sprint.mission.discodeit.dto.dto_Neo;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ReadStatusUpdateRequest( //all private final
    Instant newLastReadAt,
    Boolean newNotificationEnabled // 래퍼(null 허용)
) {
  public ReadStatusUpdateRequest(Instant newLastReadAt, Boolean newNotificationEnabled) {
    this.newLastReadAt = newLastReadAt;
    this.newNotificationEnabled = newNotificationEnabled;
  }
}
