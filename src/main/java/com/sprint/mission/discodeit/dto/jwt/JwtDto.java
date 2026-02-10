package com.sprint.mission.discodeit.dto.jwt;

import com.sprint.mission.discodeit.dto.userDto.UserDto;

public record JwtDto(
        UserDto userDto,
        String accessToken
) {
}
