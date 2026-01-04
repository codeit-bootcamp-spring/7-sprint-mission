package com.sprint.mission.discodeit.global.exception.discodietException.binaryContent;

import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class BinaryContentException extends DiscodeitException {
    public BinaryContentException(ErrorCode errorCode, String key, Object value) {
        super(errorCode, key, value);
    }

    public BinaryContentException(ErrorCode errorCode, String key, Object value, Throwable cause) {
        super(errorCode, key, value, cause);
    }
}
