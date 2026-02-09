package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserRoleUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.jwt.JwtDto;
import com.sprint.mission.discodeit.dto.response.login.LoginResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AuthService {
    public LoginResponseDto checkLoginUser(LoginRequestDto loginRequestDto);
    public UserDto updateUserRole(UserRoleUpdateRequestDto userRoleUpdateRequestDto);
    public boolean isUserOnline(UUID userId);

    @Transactional(readOnly = true)
    JwtDto createAccessToken(LoginRequestDto loginRequestDto);

    @Transactional(readOnly = true)
    String createRefreshToken(LoginRequestDto loginRequestDto);

    @Transactional(readOnly = true)
    JwtDto refreshToken(HttpServletRequest request, HttpServletResponse response);
}
