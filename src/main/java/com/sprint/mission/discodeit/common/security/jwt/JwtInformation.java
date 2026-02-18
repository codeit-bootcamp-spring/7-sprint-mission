package com.sprint.mission.discodeit.common.security.jwt;

import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;

import java.time.Instant;

public record JwtInformation(
        UserResponseDto userDto,
        String accessToken,
        Instant accessExpiresAt,
        String refreshToken,
        Instant refreshExpiresAt
) {
    public JwtInformation rotate(String newAccessToken, Instant newAccessExp,
                                 String newRefreshToken, Instant newRefreshExp) {
        return new JwtInformation(userDto, newAccessToken, newAccessExp, newRefreshToken, newRefreshExp);
    }
}
