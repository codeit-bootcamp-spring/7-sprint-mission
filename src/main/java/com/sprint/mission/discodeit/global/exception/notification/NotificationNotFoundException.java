package com.sprint.mission.discodeit.global.exception.notification;

import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class NotificationNotFoundException extends DiscodeitException {
    public NotificationNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotificationNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
