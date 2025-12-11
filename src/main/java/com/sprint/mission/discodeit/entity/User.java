package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @JoinColumn(name = "profile_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", orphanRemoval = true,
            cascade = CascadeType.ALL, optional = false)
    private UserStatus status;

    @Builder
    public User(String email, String password, String username) {
        this.username = username;       // 특수문자 불가
        this.email = email;             // 받을때 @ 있는지 확인
        this.password = password;       // 8자리 이상
        this.profile = null;
    }

    // Update
    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateProfile(BinaryContent profile) {
        this.profile = profile;
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
    }
}