package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtDto;

public interface AuthService {
    UserResponseDto updateRoleForAdmin(UserRoleUpdateRequest request);

    UserResponseDto updateRole(UserRoleUpdateRequest request);

    JwtDto reIssuerAccessToken(String refreshToken);
}
