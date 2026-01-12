package com.sprint.mission.discodeit.global.exception.discodietException.user;

import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class UserException extends DiscodeitException {
    public UserException(ErrorCode errorCode, String key, Object value) {
        super(errorCode, key, value);
    }

    public UserException(ErrorCode errorCode, String key, Object value, Throwable cause) {
        super(errorCode, key, value, cause);
    }
}
