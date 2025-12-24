package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class AuthException  extends DiscodeitException {

    public AuthException(
        ErrorCode errorCode,
        Map<String, Object> details) {

        super(errorCode, details);
    }
}
