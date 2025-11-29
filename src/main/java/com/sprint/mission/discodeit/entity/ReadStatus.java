package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "read_statuses",
    uniqueConstraints = @UniqueConstraint(
        name = "ux_read_user_channel",
        columnNames = {"user_id", "channel_id"}
    )
)
public class ReadStatus extends BaseUpdatableEntity {

  private static final long serialVersionUID = 1L;

  //Field
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false,
      foreignKey = @ForeignKey
          (name = "fk_read_user"))
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_read_channel"))
  private Channel channel;

  @Column(name = "last_read_at", nullable = false)
  private Instant readAt;

  //Constructor
  private ReadStatus(User user, Channel channel) {
    this.user = user;
    this.channel = channel;
  }

  //Factory Method
  public static ReadStatus create(User user, Channel channel) {
    return new ReadStatus(user, channel);
  }

  //사용자가 채널을 읽음.
  public void updateReadAt() {
    super.update();
    this.readAt = Instant.now();
  }
}
