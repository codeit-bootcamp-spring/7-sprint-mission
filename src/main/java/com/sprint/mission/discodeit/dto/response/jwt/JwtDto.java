package com.sprint.mission.discodeit.dto.response.jwt;

import com.sprint.mission.discodeit.dto.response.user.UserDto;

public record JwtDto(
        UserDto userDto,
        String accessToken
) {
}
