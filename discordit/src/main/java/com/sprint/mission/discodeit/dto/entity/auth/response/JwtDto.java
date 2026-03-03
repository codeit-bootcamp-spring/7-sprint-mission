package com.sprint.mission.discodeit.dto.entity.auth.response;

import com.sprint.mission.discodeit.dto.entity.user.UserDto;

public record JwtDto(
        UserDto userDto,
        String accessToken
) {
}
