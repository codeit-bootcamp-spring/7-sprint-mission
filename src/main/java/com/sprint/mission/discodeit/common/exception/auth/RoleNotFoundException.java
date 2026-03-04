package com.sprint.mission.discodeit.common.exception.auth;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;

public class RoleNotFoundException extends AuthException {
    public RoleNotFoundException(String reason) {
        super(ErrorCode.AUTH_ROLE_NOT_FOUND, Map.of("reason", reason));
    }
}

