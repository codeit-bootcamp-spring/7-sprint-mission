package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.dto.user.UserDto;

public record JwtDto(
        UserDto userDto,
        String accessToken
) {
}
