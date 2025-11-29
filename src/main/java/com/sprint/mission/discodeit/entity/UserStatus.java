package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

  private static final long serialVersionUID = 1L;

  //Field
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true, nullable = false,
      foreignKey = @ForeignKey(name = "fk_user_status_user"))
  private User user;      //접속자 UUID
  private Instant offlineAt;      //로그아웃 한 시간
  private Instant onlineAt;       //로그인 한 시간
  private boolean isOnline;
  public static final int OFFLINE_THRESHOLD_SECONDS = 60;       //오프라인 기준 타임

  //Constructor
  private UserStatus(User user) {
    this.user = user;
    this.offlineAt = Instant.now();
  }

  //Factory Method
  public static UserStatus create(User user) {
    return new UserStatus(user);
  }

  //로그인 했을 때
  public void updateOnlineAt() {
    super.update();
    this.onlineAt = Instant.now();
  }

  //종료되었을 때
  public void updateOfflineAt() {
    super.update();
    this.offlineAt = Instant.now();
  }

  //온라인 상태 계산
  public void updateOnline() {
    super.update();
    this.isOnline = offlineAt.isAfter(Instant.now().minusSeconds(OFFLINE_THRESHOLD_SECONDS));
  }
}
