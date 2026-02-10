package com.sprint.mission.discodeit.security;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void upsert(UUID userId, String tokenId, Instant expiresAt) {
        RefreshToken token = refreshTokenRepository.findById(userId)
                .orElseGet(() -> new RefreshToken(userId, tokenId, expiresAt));
        token.rotate(tokenId, expiresAt);
        refreshTokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    public Optional<RefreshToken> find(UUID userId) {
        return refreshTokenRepository.findById(userId);
    }

    @Transactional
    public void delete(UUID userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
