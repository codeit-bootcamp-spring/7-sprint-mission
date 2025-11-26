package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.AuthServiceDto;
import com.sprint.mission.discodeit.mapper.dto.UserDto;

public interface InterfaceAuthService  {
    UserDto login(AuthServiceDto authService);
}
