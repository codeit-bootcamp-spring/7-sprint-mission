package com.sprint.mission.discodeit.common.exceptions.auth;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;

import java.util.Map;

public class AuthException extends DiscodeitException {
    public AuthException(ErrorCode errorCode, String token) {
        super(null, errorCode);
        details.put("token", token);
    }

    public AuthException(ErrorCode errorCode, String token, Map<String, Object> details) {
        super(null, errorCode, details);
        this.details.putAll(details);
        this.details.put("token", token);
    }
}
