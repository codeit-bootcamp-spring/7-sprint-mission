package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

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
    private LocalDateTime lastUsed;

    public PersistentLogin(PersistentRememberMeToken token) {
        this.series =  token.getSeries(); // UUID.randomUUID().toString();
        this.username = token.getUsername();
        this.token = token.getTokenValue(); //generateToken();
        this.lastUsed = LocalDateTime.now();
    }

    // 토큰 업데이트 -> 토큰 도용 방지
    public void updateToken(String token) {
        this.token = token;
        this.lastUsed = LocalDateTime.now();
    }

    // SecureRandom을 이용해서 난수 생성
    // URL/쿠키에 안전한 Base64로 문자 인코딩
//    private String generateToken() {
//        byte[] randomBytes = new byte[16];
//        new SecureRandom().nextBytes(randomBytes);
//        return Base64.getEncoder().encodeToString(randomBytes);
//    }

}