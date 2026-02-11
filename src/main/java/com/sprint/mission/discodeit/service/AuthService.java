package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;

public interface AuthService {

  UserResponseDto updateUserRole(RoleUpdateRequest request);
}
