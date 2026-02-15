package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.RefreshToken;
import com.sprint.mission.discodeit.repository.RefreshTokenRepository;
import com.sprint.mission.discodeit.security.jwt.config.JwtProperties;
import com.sprint.mission.discodeit.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicRefreshTokenService implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public boolean exists(UUID userId, String refreshToken) {

        return refreshTokenRepository.findByUserId(userId)
                .map(token -> token.getToken().equals(refreshToken))
                .orElse(false);

    }

    @Override
    @Transactional
    public void rotateRefreshToken(UUID userId, String refreshToken) {
        refreshTokenRepository.deleteByUserId(userId);

        Instant expiresAt = Instant.now().plusSeconds(jwtProperties.getRefreshTokenExpirationMs() / 1000);

        refreshTokenRepository.save(new RefreshToken(
                        userId,
                        refreshToken,
                        expiresAt
                )
        );
    }

    @Override
    public void revokeAll(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void saveRefreshToken(UUID userId, String refreshToken) {
        refreshTokenRepository.deleteByUserId(userId);

        Instant expiresAt = Instant.now().plusSeconds(jwtProperties.getRefreshTokenExpirationMs() / 1000);

        refreshTokenRepository.save(new RefreshToken(
                        userId,
                        refreshToken,
                        expiresAt
                )
        );


    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
