package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface RefreshTokenService {

    boolean exists(UUID userId, String refreshToken);

    void rotateRefreshToken(UUID userId, String refreshToken);

    void revokeAll(UUID userId);

    void saveRefreshToken(UUID userId, String refreshToken);

    void deleteRefreshToken(String refreshToken);
}
