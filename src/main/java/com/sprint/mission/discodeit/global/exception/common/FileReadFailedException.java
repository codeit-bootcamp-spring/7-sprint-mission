package com.sprint.mission.discodeit.global.exception.common;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class FileReadFailedException extends CommonException {
    public FileReadFailedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FileReadFailedException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
