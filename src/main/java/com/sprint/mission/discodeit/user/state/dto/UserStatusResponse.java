package com.sprint.mission.discodeit.user.state.dto;

import com.sprint.mission.discodeit.user.state.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
    UUID id,
    Instant createdAt,
    Instant updateAt,
    UUID userId,
    Instant lastActiveAt,
    boolean online
) {

  public static UserStatusResponse fromEntity(UserStatus userStatus) {
    return new UserStatusResponse(userStatus.getId(),
        userStatus.getCreatedAt(),
        userStatus.getUpdatedAt(),
        userStatus.getUserId(),
        userStatus.getLastOnlineAt(),
        true);
  }


}
