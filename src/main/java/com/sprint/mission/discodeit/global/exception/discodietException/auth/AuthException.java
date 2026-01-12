package com.sprint.mission.discodeit.global.exception.discodietException.auth;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;

public class AuthException extends DiscodeitException {
    public AuthException(ErrorCode errorCode, String key, Object value) {
        super(errorCode, key, value);
    }

    public AuthException(ErrorCode errorCode, String key, Object value, Throwable cause) {
        super(errorCode, key, value, cause);
    }
}
