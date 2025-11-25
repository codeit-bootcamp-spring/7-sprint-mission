package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.enums.UserStatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;


  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;

  public UserStatus(User user, Instant lastActiveAt) {
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public void update(Instant lastActiveAt) {
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
    }
  }

  public UserStatusType isOnline() {
    if (Duration.between(lastActiveAt, Instant.now()).toSeconds() <= 300) {
      return UserStatusType.ONLINE;
    }
    return UserStatusType.OFFLINE;
  }
}
