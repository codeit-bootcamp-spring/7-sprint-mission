package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
public class RefreshToken extends BasicEntity {

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;


    public RefreshToken(UUID userId, String token, Instant expiresAt) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    /**
     * 만료 여부 확인
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }


}
