package com.sprint.mission.discodeit.security.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name = "persistent_logins",
        indexes = @Index(name = "idx_persistent_logins_username", columnList = "username"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersistentLogin {
    @Id
    @Column(length = 64, nullable = false)
    private String series;

    @Column(length = 64, nullable = false)
    private String username;

    @Column(length = 64, nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime lastUsed;

    public PersistentLogin(String series, String token, String username) {
        this.series = series;
        this.username = username;
        this.token = token;
        this.lastUsed = LocalDateTime.now();
    }

    public void updateToken(String token) {
        this.token = token;
        this.lastUsed = LocalDateTime.now();
    }
}
