package com.sprint.mission.discodeit.global.exception.discodietException.readStatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;

public class ReadStatusException extends DiscodeitException {
    public ReadStatusException(ErrorCode errorCode, String key, Object value) {
        super(errorCode, key, value);
    }

    public ReadStatusException(ErrorCode errorCode, String key, Object value, Throwable cause) {
        super(errorCode, key, value, cause);
    }
}
