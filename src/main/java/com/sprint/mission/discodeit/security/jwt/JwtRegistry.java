package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.mapper.dto.JwtInformation;
import java.util.UUID;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public interface JwtRegistry {

    void registerJwtInformation(JwtInformation jwtInformation);

    void invalidateJwtInformationByUserId(UUID userId);

    boolean hasActiveJwtInformationByUserId(UUID userId);

    boolean hasActiveJwtInformationByAccessToken(String accessToken);

    boolean hasActiveJwtInformationByRefreshToken(String refreshToken);

    void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation);

    void clearExpiredJwtInformation();
}
