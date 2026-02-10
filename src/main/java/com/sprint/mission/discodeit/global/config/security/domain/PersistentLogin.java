package com.sprint.mission.discodeit.global.config.security.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Entity
@Table(name = "persistent_logins",
        indexes = @Index(name = "idx_persistent_logins_username", columnList = "username"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersistentLogin {

    @Id
    @Column(length = 64, nullable = false)
    private String series;

    @Column(nullable = false, length = 64)
    private String username;

    @Column(nullable = false, length = 64)
    private String token;

    @Column(nullable = false)
    private Instant lastUsed;

    @Builder
    public PersistentLogin(String username, String series, String token) {
        this.series = series;
        this.username = username;
        this.token = token;
        this.lastUsed = Instant.now();
    }

    // 토큰 업데이트 -> 토큰 도용 방지
    public void updateToken(String token, Instant lastUsed) {
        this.token = token;
        this.lastUsed = lastUsed;
    }

    // SecureRandom을 이용해서 난수 생성
    // URL/쿠키에 안전한 Base64로 문자 인코딩
    private String generateToken() {
        byte[] randomBytes = new byte[16];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

}