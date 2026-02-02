package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserRoleUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.login.LoginResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;

import java.util.UUID;

public interface AuthService {
    public LoginResponseDto checkLoginUser(LoginRequestDto loginRequestDto);
    public UserDto updateUserRole(UserRoleUpdateRequestDto userRoleUpdateRequestDto);
    public boolean isUserOnline(UUID userId);
}
