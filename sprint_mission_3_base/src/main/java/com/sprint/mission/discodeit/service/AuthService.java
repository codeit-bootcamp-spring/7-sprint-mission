package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
