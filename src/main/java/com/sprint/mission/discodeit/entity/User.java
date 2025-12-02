package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

  //Field
  @Column(name = "email", length = 100, unique = true, nullable = false)
  private String email;               //이메일

  @Column(name = "username", length = 50, unique = true, nullable = false)
  private String nickname;            //닉네임

  @Column(name = "password", length = 60, nullable = false)
  private String password;            //비밀번호

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id", unique = true,
      foreignKey = @ForeignKey(name = "fk_profile"))
  private BinaryContent profile;             //프로필 이미지 UUID

  //Constructor
  private User(String email, String nickname, String password, BinaryContent profile) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.profile = profile;
  }

  //Factory Method
  public static User createWithProfile(String email, String nickname, String password,
      BinaryContent profile) {
    return new User(email, nickname, password, profile);
  }

  public static User createWithoutProfile(String email, String nickname, String password) {
    return new User(email, nickname, password, null);
  }

  //update
  public User update(String email, String nickname, String password) {
    super.update();
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    return this;
  }

  //profile update
  public User updateProfile(BinaryContent profile) {
    super.update();
    this.profile = profile;
    return this;
  }

  //임시 비밀번호로 업데이트
  public User updateTemporaryPassword(String password) {
    super.update();
    this.password = password;
    return this;
  }
}