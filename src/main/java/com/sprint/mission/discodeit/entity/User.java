package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends Common {

  @Serial
  private static final long serialVersionUID = 1L;
  public Instant updateAt;

  private String username;
  private String email;
  private String password;
  private UUID profileId;

  @ToString.Exclude
  private final List<Channel> joinChannels;

  public User(String username, String email, String password, UUID profileId) {
    this.updateAt = Instant.now();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
    joinChannels = new ArrayList<>();
  }

  // equlas에서 발생할 npe에 대한 문제
  // username!= null이 통과하면 username은 null이 아니라는 보장성이 생김
  // this.username은 null일 가능성이 있고, null.equals는 npe 발생
  public void updateUser(String username, String password, String email, UUID profileId) {
    boolean isUpdate = false;
    if (username != null && !username.equals(this.username)) {
      this.username = username;
      isUpdate = true;
    }
    if (password != null && !password.equals(this.password)) {
      this.password = password;
      isUpdate = true;
    }
    if (email != null && !this.email.equals(email)) {
      this.email = email;
      isUpdate = true;
    }

    if (profileId != null && !profileId.equals(this.profileId)) {
      this.profileId = profileId;
      isUpdate = true;
    }

    if (isUpdate) {
      updateAt = Instant.now();
    }
  }

  public void joinChannel(Channel channel) {
    if (channel != null && !joinChannels.contains(channel)) {
      joinChannels.add(channel);
      updateAt = Instant.now();
    }
  }

  public void leaveChannel(Channel channel) {
    if (channel != null && joinChannels.contains(channel)) {
      joinChannels.remove(channel);
      updateAt = Instant.now();
    }
  }
}
