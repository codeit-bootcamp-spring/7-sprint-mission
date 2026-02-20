package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.security.jwt.JwtDto;
import org.springframework.data.util.Pair;

public interface AuthService {

    UserResponseDto updateRole(UserRoleUpdateRequest request);

    Pair<JwtDto, String> refreshAccessToken(String refreshToken);
}
