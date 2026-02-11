package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.JwtInformation;

import java.util.UUID;

public interface JwtRegistry {
    void registerJwtInformation(JwtInformation jwtInformation);
    void invalidateJwtInformationByUserId(UUID userId);
    boolean hasActiveJwtInformationByUserId(UUID userId);
    boolean hasActiveJwtInformationByAccessToken(String accessToken);
    boolean hasActiveJwtInformationByRefreshToken(String refreshToken);
    void rotateJwtInformation(String refreshToken, JwtInformation jwtInformation);
    void clearExpiredJwtInformation();
}
