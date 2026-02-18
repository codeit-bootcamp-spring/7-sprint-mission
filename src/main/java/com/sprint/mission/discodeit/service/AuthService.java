package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtInformation;

public interface AuthService {
    UserResponseDto updateRoleForAdmin(UserRoleUpdateRequest request);

    UserResponseDto updateRole(UserRoleUpdateRequest request);

    JwtInformation reIssuerAccessToken(String refreshToken);
}
