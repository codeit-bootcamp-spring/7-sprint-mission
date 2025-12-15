package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity{

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // 프로필 삭제시 profile_id 컬럼을 null로 세팅
    // 유저 삭제시 프로필은 함께 삭제
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "profile_id",
            unique = true,
            foreignKey = @ForeignKey(
                    name = "fk_user_profile",
                    foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES binary_contents(id) ON DELETE SET NULL"
            )
    )
    private BinaryContent profile;

    // DB 테이블에 저장하지 않는 필드
    // mappedBy 속성 적용시 외래키를 가지고 있지 않아
    // 객체 존재 확인을 위해 기본적을 EAGER로 동작
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus status;

    public User(String username, String email, String password, BinaryContent profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void update(String username, String email, String password, BinaryContent profile) {
        if (username != null && !username.equals(this.username)) {
            this.username = username;
        }
        if (email != null && !email.equals(this.email)) {
            this.email = email;
        }
        if (password != null && !password.equals(this.password)) {
            this.password = password;
        }
        if (profile != null && !profile.equals(this.profile)) {
            this.profile = profile;
        }
    }

    @Override
    public String toString() {
        String str = super.toString();

        return "User{" +
                str +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
