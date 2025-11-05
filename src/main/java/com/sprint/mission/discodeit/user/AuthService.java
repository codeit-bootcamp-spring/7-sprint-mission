package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.user.dto.AuthUserDTO;
import com.sprint.mission.discodeit.user.dto.LoginRequestDTO;

public interface AuthService {
    AuthUserDTO login(LoginRequestDTO request);
}
