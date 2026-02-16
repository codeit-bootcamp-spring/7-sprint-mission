package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.UUID;

public record RefreshToken(
        UUID id,
        String token,
        UUID userId,
        Instant expiresAt,
        Instant createdAt

) {
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
