package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtInformation {
    private UserResponseDto userDto;
    private String accessToken;
    private String refreshToken;

    public void rotate(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
