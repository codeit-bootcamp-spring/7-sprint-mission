package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.user.dto.AuthUserDTO;

public interface AuthService {
    AuthUserDTO login(String username, String password);
}
