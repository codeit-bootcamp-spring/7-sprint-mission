package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User extends BaseUpdatableEntity {

  private String username; // 유저 이름
  private String email; // 이메일
  private String password; // 유저 비밀번호
  private UUID profileId;


  public User(String username, String email, String password, UUID profileId) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }

  public void updateProfile(UUID newProfileId) {
    this.profileId = newProfileId;
  }

  public void updateInfo(String newUsername, String newEmail, String newPassword) {
    this.username = newUsername;
    this.email = newEmail;
    this.password = newPassword;
  }
}