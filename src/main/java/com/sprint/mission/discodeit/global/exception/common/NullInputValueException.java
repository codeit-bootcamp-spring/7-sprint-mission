package com.sprint.mission.discodeit.global.exception.common;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class NullInputValueException extends CommonException {
    public NullInputValueException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NullInputValueException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
