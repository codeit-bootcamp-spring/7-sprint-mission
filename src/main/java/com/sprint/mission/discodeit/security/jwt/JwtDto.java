package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;

public record JwtDto(
        UserResponseDto userDto,
        String accessToken
) {}
