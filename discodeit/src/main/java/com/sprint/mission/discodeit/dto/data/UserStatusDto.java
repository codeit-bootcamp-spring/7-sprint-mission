package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;

public record UserStatusDto (
  UUID id,
  UUID userId,
  String status
){
    public static UserStatusDto from(UserStatus status) {
      return new UserStatusDto(
          status.getId(),
          status.getUserId(),
          status.getStatus()
      );
    }
}
