package com.sprint.mission.discodeit.global.exception.discodietException.notification;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;

public class NotificationException extends DiscodeitException {
    public NotificationException(ErrorCode errorCode, String key, Object value) {
        super(errorCode, key, value);
    }

    public NotificationException(ErrorCode errorCode, String key, Object value, Throwable cause) {
        super(errorCode, key, value, cause);
    }
}
