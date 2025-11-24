package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true)
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant lastAccessTime; //마지막 접속 시간

  public UserStatus(User user) {
    this.user = user;
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
