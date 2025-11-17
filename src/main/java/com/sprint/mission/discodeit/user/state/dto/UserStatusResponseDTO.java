package com.sprint.mission.discodeit.user.state.dto;

import com.sprint.mission.discodeit.config.enums.Status;

import com.sprint.mission.discodeit.user.state.UserStatus;
import java.time.Instant;

public record UserStatusResponseDTO(
    String id,
    Status currentStatus,
    String message,
    Instant lastOnlineAt
) {

  public static UserStatusResponseDTO fromEntity(UserStatus UserStatus) {
    return new UserStatusResponseDTO(
        UserStatus.getId().toString(),
        UserStatus.getCurrentStatus(),
        UserStatus.getCustomStatusMessage(),
        UserStatus.getLastOnlineAt()
    );
  }
}
