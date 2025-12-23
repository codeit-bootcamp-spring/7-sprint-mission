package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class AuthenticationFailedException extends AuthException {
    public AuthenticationFailedException(String username) {
        super(ErrorCode.AUTHENTICATION_FAILED,
                Map.of("username", username));
    }
}
