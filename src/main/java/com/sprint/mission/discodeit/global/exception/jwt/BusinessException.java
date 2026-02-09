package com.sprint.mission.discodeit.global.exception.jwt;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class BusinessException extends DiscodeitException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
