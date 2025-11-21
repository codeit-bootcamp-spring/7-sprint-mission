package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.response.login.LoginResponseDto;

public interface AuthService {
    public LoginResponseDto checkLoginUser(LoginRequestDto loginRequestDto);

}
