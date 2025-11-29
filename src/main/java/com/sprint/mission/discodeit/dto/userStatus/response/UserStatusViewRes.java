package com.sprint.mission.discodeit.dto.userStatus.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.DateTimeUtil;

import java.util.UUID;

public record UserStatusViewRes(
    UUID userId,
    boolean isOnline,
    String lastOfflineAt
) {

  public static UserStatusViewRes from(UserStatus userStatus) {
    return new UserStatusViewRes(
        userStatus.getUser().getId(),
        userStatus.isOnline(),
        DateTimeUtil.format(userStatus.getOfflineAt())
    );
  }
}
