package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.enums.UserStatusType;
import lombok.Getter;

import java.io.Serial;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Getter
public class UserStatus extends Common {

  @Serial
  private static final long serialVersionUID = 1L;
  private Instant updateAt;
  private final UUID userId;
  private Instant lastActiveAt;

  public UserStatus(UUID userId, Instant lastActiveAt) {
    this.updateAt = Instant.now();
    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }

  public void update(Instant lastActiveAt) {
    boolean isUpdate = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      isUpdate = true;
    }

    if (isUpdate) {
      updateAt = Instant.now();
    }
  }

  public UserStatusType isOnline() {
    if (Duration.between(lastActiveAt, Instant.now()).toSeconds() <= 300) {
      return UserStatusType.ONLINE;
    }
    return UserStatusType.OFFLINE;
  }


}
