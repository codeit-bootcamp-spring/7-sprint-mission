package com.sprint.mission.discodeit.dto.response.auth;

public record AuthTokenResponseDto(
        String accessToken,
        String refreshToken
) {
}
