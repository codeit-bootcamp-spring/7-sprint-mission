package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends BasicEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String username; // 유저 이름
  private String email; // 이메일
  private String password; // 유저 비밀번호
  private UUID profileId;


  public User(String username, String email, String password, UUID profileId) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }

  public void updateProfile(UUID NewProfileId) {
    this.profileId = NewProfileId;
    update();
  }

  public void updateInfo(String newUsername, String newEmail, String newPassword) {
    this.username = newUsername;
    this.email = newEmail;
    this.password = newPassword;
    update();
  }
}