package com.sprint.mission.discodeit.user.state;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import com.sprint.mission.discodeit.config.enums.Status;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
public class UserStatus extends BaseEntity<UUID> {


  private final UUID userId;
  /**
   * 사용자의 현재 상태 (ONLINE, OFFLINE 등)
   */
  private Status currentStatus;


  private String customStatusMessage;

  /**
   * 마지막으로 온라인이었던 시간 (Unix Timestamp)
   */
  private Instant lastOnlineAt;

  /**
   * 자식 클래스로부터 생성된 ID를 전달받는 생성자입니다. ID 생성의 책임을 자식에게 위임합니다.
   */
  private UserStatus(UUID userId) {
    super(UUID.randomUUID());
    if (userId == null) {
      throw new IllegalArgumentException("사용자 정보를 확인 할 수 없습니다.");
    }
    this.userId = userId;
    this.currentStatus = Status.OFFLINE;
  }

  public static UserStatus create(UUID userId) {
    return new UserStatus(userId);
  }

  public UserStatus updateLastOnlineAt(Instant lastOnlineAt) {
    this.lastOnlineAt = lastOnlineAt;
    super.updateTimestamp();
    return this;
  }

  /**
   * '온라인이 된다'는 비즈니스 이벤트를 처리합니다. 상태와 마지막 접속 시간을 함께 변경하여 데이터의 일관성을 유지합니다.
   */
  public void setOnline() {
    this.currentStatus = Status.ONLINE;
    this.lastOnlineAt = Instant.now();
    super.updateTimestamp();
  }

  /**
   * '오프라인이 된다'는 비즈니스 이벤트를 처리합니다.
   */
  public void setOffline() {
    this.currentStatus = Status.OFFLINE;
    super.updateTimestamp();
  }

  /**
   * '자리 비움으로 설정한다'는 비즈니스 이벤트를 처리합니다. 오프라인 상태에서는 변경할 수 없다는 비즈니스 규칙을 포함합니다.
   */
  public void setAway() {
    if (this.isOffline()) {
      throw new IllegalStateException("오프라인 상태에서는 자리 비움으로 변경할 수 없습니다.");
    }
    this.currentStatus = Status.AFK;
    super.updateTimestamp();
  }

  /**
   * '방해 금지로 설정한다'는 비즈니스 이벤트를 처리합니다.
   */
  public void setDoNotDisturb(String message) {
    if (this.isOffline()) {
      throw new IllegalStateException("오프라인 상태에서는 방해 금지로 변경할 수 없습니다.");
    }
    if (message == null || !message.equals(this.customStatusMessage) || message.isBlank()) {
      this.customStatusMessage = message;
    }
    this.currentStatus = Status.DND;
    super.updateTimestamp();
  }

  // --- 상태 확인 편의 메서드 ---

  public boolean isOnline() {
    return this.currentStatus == Status.ONLINE;
  }

  public boolean isOffline() {
    return this.currentStatus == Status.OFFLINE;
  }

  public boolean isAway() {
    return this.currentStatus == Status.AFK;
  }

  public boolean isDoNotDisturb() {
    return this.currentStatus == Status.DND;
  }

}
