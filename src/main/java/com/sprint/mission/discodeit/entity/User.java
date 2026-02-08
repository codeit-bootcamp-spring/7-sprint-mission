package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class User extends BaseUpdatableEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;


    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true, fetch = FetchType.LAZY)
    //REMOVE-> user삭제 시 binary_content 삭제 쿼리 날림
    //만약에 binary_contents가 user_id를 가지고 있었다면 on delete cascade로 처리 가능
    //orphanRemoval = true -> 프로필 교체 시 원래 참조하던 binary_content 삭제 쿼리 날림
    //이 기능은 jpa 기능이고 db에선 구현 불가(트리거는 요즘 안 쓰니까)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @Enumerated(EnumType.STRING)
    private Role role;


    public User(String email, String password, String username) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role=Role.USER;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateProfile(BinaryContent profile) {
        this.profile = profile;
    }

    public void updateRole(Role role){this.role = role;}
}
