package com.sprint.mission.discodeit.common.exception.user;

import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;

import java.util.Map;

public class UserException extends DiscodeitException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    public UserException(ErrorCode errorCode, Map<String, Object> details, Throwable cause) {
        super(errorCode, details, cause);
    }
}
