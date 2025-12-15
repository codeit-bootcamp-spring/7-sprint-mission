package com.sprint.mission.discodeit.global.exception.discodietException.message;

import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class MessageException extends DiscodeitException {
    public MessageException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MessageException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
