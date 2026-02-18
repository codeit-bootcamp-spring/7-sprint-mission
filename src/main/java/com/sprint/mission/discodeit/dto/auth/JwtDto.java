package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;

public record JwtDto(
        UserResponseDto userDto,
        String accessToken
) {
}
