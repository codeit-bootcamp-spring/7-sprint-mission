package com.sprint.mission.discodeit.dto.response.jwt;

import com.sprint.mission.discodeit.dto.response.user.UserDto;

public record JwtInformation(
        UserDto userDto,
        String accessToken,
        String refreshToken

) {

    public void rotate (String accessToken, String refreshToken) {
    }
}
