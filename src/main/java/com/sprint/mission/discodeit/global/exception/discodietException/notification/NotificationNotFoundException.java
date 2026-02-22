package com.sprint.mission.discodeit.global.exception.discodietException.notification;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.UUID;

public class NotificationNotFoundException extends NotificationException {
    public NotificationNotFoundException(String key, Object value) {
        super(ErrorCode.NOTIFICATION_FORBIDDEN, key, value);
    }

    public static NotificationNotFoundException byId(UUID notificationId) {
        return new NotificationNotFoundException("notificationId", notificationId);
    }
}
