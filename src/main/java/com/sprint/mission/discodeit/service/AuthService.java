package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.loginDto.LoginRequest;

public interface AuthService {
    User login(LoginRequest loginRequestDto);
}
