package com.sprint.mission.discodeit.global.exception.common;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public abstract class CommonException extends DiscodeitException {
    public CommonException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CommonException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
