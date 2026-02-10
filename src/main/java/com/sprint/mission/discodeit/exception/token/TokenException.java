package com.sprint.mission.discodeit.exception.token;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class TokenException extends DiscodeitException {
    public TokenException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
