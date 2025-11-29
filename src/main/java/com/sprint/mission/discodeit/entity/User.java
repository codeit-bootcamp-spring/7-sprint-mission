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

import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

  @Column(length = 50, nullable = false, unique = true)
  private String username;

  @Column(length = 100, nullable = false, unique = true)
  private String email;

  @Column(length = 60, nullable = false)
  private String password;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id", unique = true)
  private BinaryContent profile;

  @OneToOne(mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private UserStatus status;


  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public void assignStatus(UserStatus status) {
    this.status = status;
  }

  public void assignProfile(BinaryContent profile) {
    this.profile = profile;
  }

  public void updateProfile(BinaryContent newProfile) {
    this.profile = newProfile;
  }

  public void updateInfo(String newUsername, String newEmail, String newPassword) {
    if (newUsername != null) {
      this.username = newUsername;
    }
    if (newEmail != null) {
      this.email = newEmail;
    }
    if (newPassword != null) {
      this.password = newPassword;
    }
  }
}