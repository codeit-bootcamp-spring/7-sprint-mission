package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.mapper.dto.LoginRequest;
import com.sprint.mission.discodeit.mapper.dto.UserDto;

public interface InterfaceAuthService  {
    UserDto login(LoginRequest authService);
}
