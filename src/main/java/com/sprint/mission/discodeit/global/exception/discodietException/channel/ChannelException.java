package com.sprint.mission.discodeit.global.exception.discodietException.channel;

import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

public class ChannelException extends DiscodeitException {

    public ChannelException(ErrorCode errorCode, String key, Object value) {
        super(errorCode, key, value);
    }

    public ChannelException(ErrorCode errorCode, String key, Object value, Throwable cause) {
        super(errorCode, key, value, cause);
    }
}
