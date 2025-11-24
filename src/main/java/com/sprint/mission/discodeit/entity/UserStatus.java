package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

  private UUID userId; //유저 ID
  private Instant lastAccessTime; //마지막 접속 시간

  public UserStatus(UUID userId) {
    super();
    this.userId = userId;
    this.lastAccessTime = Instant.now();
  }

  // 마지막 접속 시간이 현재 시간 5분 이내면 접속 중인 유저 판단
  public boolean isOnline() {
    Instant fiveMinutes = Instant.now().minus(Duration.ofMinutes(5));
    return lastAccessTime.isAfter(fiveMinutes);
  }

  // 유저 상태 업데이트
  public void statusUpdate(Instant newTime) {
    this.lastAccessTime = newTime;
  }
}
