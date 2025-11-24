package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username; // 유저 이름 ( 별명 x)

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus status;

    protected User() {}

    public User(String username, String password, String email, BinaryContent profile) {
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
        this.email = Objects.requireNonNull(email);
        this.profile = profile;
    }

    public void setUsername(String username) {
        String v = VerifiedUtils.verifyName(username);
        if (!v.equals(this.username)) {
            this.username = v;
        }
    }

    public void setPassword(String password) {
        String v = VerifiedUtils.verifyPassword(password);
        if (!v.equals(this.password)) {
            this.password = v;
        }
    }


    public void setEmail(String email) {
        String v = VerifiedUtils.verifyEmail(email);
        if (!v.equals(this.email)) {
            this.email = v;
        }
    }

    public void setProfile(BinaryContent profile) {
        if(this.profile != profile) {
            this.profile = profile;
        }
    }

    public void update(String username, String password, String email) {
        if (username != null) setUsername(username);
        if (password != null) setPassword(password);
        if (email != null) setEmail(email);
    }

    public boolean passwordMatch(String password) {
        return this.password.equals(password);
    }
}
