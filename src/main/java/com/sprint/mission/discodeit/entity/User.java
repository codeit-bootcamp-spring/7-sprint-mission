package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.common.exception.user.InvalidUserRequestException;
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

    @Enumerated(EnumType.STRING)
    private UserRole role;

    protected User() {}

    public User(String username, String passwordHash, String email, BinaryContent profile, UserRole role) {
        this.username = Objects.requireNonNull(username);
        setPasswordHash(Objects.requireNonNull(passwordHash));
        this.email = Objects.requireNonNull(email);
        this.profile = profile;
        this.role = Objects.requireNonNull(role);
    }

    public void setUsername(String username) {
        if (username.length() > 20 || username.length() < 2) {
            throw new InvalidUserRequestException("name length must be between 2 and 20 characters");
        }
        if (!username.equals(this.username)) {
            this.username = username;
        }
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null) {
            throw new InvalidUserRequestException("password is null");
        }
        boolean looksLikeBcrypt = passwordHash.startsWith("$2a$") || passwordHash.startsWith("$2b$") || passwordHash.startsWith("$2y$");
        if (!looksLikeBcrypt || passwordHash.length() != 60) {
            throw new InvalidUserRequestException("password length must be between 7 and 50 characters");
        }
        if (!passwordHash.equals(this.password)) {
            this.password = passwordHash;
        }
    }

    public void setEmail(String email) {
        String s = email.trim();
        if(!s.contains("@")) {
            throw new InvalidUserRequestException("email must contain '@'");
        }
        if (!s.equals(this.email)) {
            this.email = s;
        }
    }

    public void setProfile(BinaryContent profile) {
        if(this.profile != profile) {
            this.profile = profile;
        }
    }

    public void update(String username, String email) {
        if (username != null) setUsername(username);
        if (email != null) setEmail(email);
    }

    public void updateRole(UserRole role) {
        this.role = Objects.requireNonNull(role);
    }
}
