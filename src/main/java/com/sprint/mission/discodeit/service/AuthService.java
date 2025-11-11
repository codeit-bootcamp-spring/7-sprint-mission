package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.login.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.login.response.LoginResponseDto;


public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
