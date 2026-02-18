package com.sprint.mission.discodeit.dto.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sprint.mission.discodeit.dto.userDto.UserDto;

public record JwtDto(
        UserDto userDto,
        String accessToken,

        @JsonIgnore
        String refreshToken
) {
}
