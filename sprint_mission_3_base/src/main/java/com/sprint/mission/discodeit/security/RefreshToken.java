package com.sprint.mission.discodeit.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 64)
    private String tokenId;

    @Column(nullable = false)
    private Instant expiresAt;

    protected RefreshToken() {
    }

    public RefreshToken(UUID userId, String tokenId, Instant expiresAt) {
        this.userId = userId;
        this.tokenId = tokenId;
        this.expiresAt = expiresAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void rotate(String tokenId, Instant expiresAt) {
        this.tokenId = tokenId;
        this.expiresAt = expiresAt;
    }
}
