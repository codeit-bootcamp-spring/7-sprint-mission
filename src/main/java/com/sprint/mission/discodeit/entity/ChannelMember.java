package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "channel_members",
    uniqueConstraints = @UniqueConstraint(
        name = "ux_read_user_channel",
        columnNames = {"user_id", "channel_id"}
    )
)
public class ChannelMember extends BaseUpdatableEntity {

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

  @Enumerated(EnumType.STRING)
  @Column(name = "role", length = 20, nullable = false)
  private ChannelMemberRole role;

  //Constructor
  private ChannelMember(User user, Channel channel, ChannelMemberRole role) {
    this.user = user;
    this.channel = channel;
    this.role = role;
  }

  //Factory Method
  public static ChannelMember create(User user, Channel channel, ChannelMemberRole role) {
    return new ChannelMember(user, channel, role);
  }

  //사용자가 채널을 읽음.
  public void updateReadAt() {
    super.update();
    this.readAt = Instant.now();
  }
}
