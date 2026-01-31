package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    // CascadeType.REMOVE는 부모가 삭제될 때만 작동
    // orphanRemoval은 부모와의 관계가 끊기면 작동

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", unique = true)
    private BinaryContent profile;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus userStatus;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String email, String password, BinaryContent profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.userStatus = new UserStatus(this, Instant.now()); // 영속성 전이로, UserStatus 생성
        this.role = Role.USER;
    }

    public void updateRole(Role role) {
        this.role = role;
    }


    // equals에서 발생할 npe에 대한 문제
    // username!= null이 통과하면 username은 null이 아니라는 보장성이 생김
    // this.username은 null일 가능성이 있고, null.equals는 npe 발생
    public void updateUser(String username, String password, String email, BinaryContent profile) {
        if (username != null && !username.equals(this.username)) {
            this.username = username;
        }
        if (password != null && !password.equals(this.password)) {
            this.password = password;
        }
        if (email != null && !email.equals(this.email)) {
            this.email = email;
        }
        if (profile != null && !profile.equals(this.profile)) {
            this.profile = profile;
        }
    }
}
