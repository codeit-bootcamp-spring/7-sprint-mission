package com.sprint.mission.discodeit.jwt;

import com.sprint.mission.discodeit.service.dto.response.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtDto {
    private UserDto userDto;
    private String accessToken;
}
