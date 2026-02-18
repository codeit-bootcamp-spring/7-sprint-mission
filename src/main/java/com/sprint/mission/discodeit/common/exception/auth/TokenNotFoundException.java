package com.sprint.mission.discodeit.common.exception.auth;

import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;

public class TokenNotFoundException extends AuthException {
    public TokenNotFoundException(String reason) {
        super(ErrorCode.AUTH_TOKEN_NOT_FOUND, Map.of("reason", reason));
    }
}
