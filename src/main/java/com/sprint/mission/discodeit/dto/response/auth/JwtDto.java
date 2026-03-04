package com.sprint.mission.discodeit.dto.response.auth;

import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;

public record JwtDto(
        UserResponseDto userDto,
        String accessToken
) {
}
