package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.login.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;


public interface AuthService {

  UserResponseDto login(LoginRequestDto loginRequestDto);
}
