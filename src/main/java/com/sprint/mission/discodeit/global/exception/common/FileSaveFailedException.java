package com.sprint.mission.discodeit.global.exception.common;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class FileSaveFailedException extends CommonException {
    public FileSaveFailedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FileSaveFailedException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
