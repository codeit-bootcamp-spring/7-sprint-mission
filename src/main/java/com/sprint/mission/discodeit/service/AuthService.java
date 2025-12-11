package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
import com.sprint.mission.discodeit.dto.request.LoginRequestDto;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {

  LoginResponseDto login(LoginRequestDto request);
}
