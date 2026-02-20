package com.sprint.mission.discodeit.exception.domain.notification;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.domain.DiscodeitException;

import java.util.Map;

public class NotificationException extends DiscodeitException {

    NotificationException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
