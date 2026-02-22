package com.sprint.mission.discodeit.global.exception.discodietException.notification;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class NotificationForbiddenException extends NotificationException {
    public NotificationForbiddenException(String key, Object value) {
        super(ErrorCode.NOTIFICATION_NOT_FOUND, key, value);
    }

    public static NotificationForbiddenException byId(UUID notificationId) {
        return new NotificationForbiddenException("notificationId", notificationId);
    }
}
