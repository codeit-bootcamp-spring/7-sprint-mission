package com.sprint.mission.discodeit.global.exception.discodietException.auth;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class JwtTokenException extends AuthException {

    public JwtTokenException(ErrorCode errorCode, String key, Object value) {
        super(errorCode, key, value);
    }

    public static JwtTokenException byInvalidToken(ErrorCode errorCode, String token) {
        return new JwtTokenException(errorCode, "token", token);
    }
}
