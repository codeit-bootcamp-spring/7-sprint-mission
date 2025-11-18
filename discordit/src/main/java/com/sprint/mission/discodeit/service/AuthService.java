package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.auth.response.UserLoginResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;

public interface AuthService {
    UserLoginResponse login(UserLoginRequest dto);
}
