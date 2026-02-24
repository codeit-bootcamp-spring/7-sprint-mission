package com.sprint.mission.discodeit.dto.dto_Neo;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ReadStatusUpdateRequest( //all private final
    @NotNull Instant newLastReadAt,
    @NotNull boolean newNotificationEnabled
) {
  public ReadStatusUpdateRequest(Instant newLastReadAt, boolean newNotificationEnabled) {
    this.newLastReadAt = newLastReadAt;
    this.newNotificationEnabled = newNotificationEnabled;
  }
}
