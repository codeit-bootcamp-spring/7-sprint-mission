package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.loginDto.LoginRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;

public interface AuthService {
    UserDto login(LoginRequest loginRequestDto);
}
