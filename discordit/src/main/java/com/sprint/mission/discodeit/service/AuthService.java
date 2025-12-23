package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.auth.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.entity.auth.response.UserLoginResponse;

public interface AuthService {
    UserLoginResponse login(UserLoginRequest dto);
}
