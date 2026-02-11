package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.userDto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtInformation {

    private UserDto userDto;
    private String accessToken;
    private String refreshToke;

    public void rotate(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToke = refreshToken;
    }
}
