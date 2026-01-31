package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;

public interface AuthService {
    UserResponseDto updateRole(UserRoleUpdateRequest request);
}
