package com.sprint.mission.discodeit.global.exception.discodietException.message;

import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class MessageException extends DiscodeitException {
    public MessageException(ErrorCode errorCode, String key, Object value) {
        super(errorCode, key, value);
    }

    public MessageException(ErrorCode errorCode, String key, Object value, Throwable cause) {
        super(errorCode, key, value, cause);
    }
}
