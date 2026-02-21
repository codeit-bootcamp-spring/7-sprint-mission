package com.sprint.mission.discodeit.mapper.dto;

public record JwtInformation(
    UserDto userDto,
    String accessToken,
    String refreshToken
) {
    // Note: Refresh token will be stored in HttpOnly cookie, not in response body
}