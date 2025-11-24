package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadStatus extends BaseUpdatableEntity {

  private UUID userId; //유저 ID
  private UUID channelId; //채널 ID
  private Instant lastReadAt; // 마지막 읽은 시간

  public ReadStatus(UUID userId, UUID channelId) {
    super();
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadAt = Instant.now();
  }

  // 업데이트하기
  public void updateReadTime(Instant newTime) {
    this.lastReadAt = newTime;
  }
}
