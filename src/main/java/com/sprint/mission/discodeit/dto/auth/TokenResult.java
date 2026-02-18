package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;

public record TokenResult(
        UserResponseDto user,
        String accessToken,
        String refreshToken
) {}
