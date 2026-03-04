package com.sprint.mission.discodeit.common.exceptions.notification;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.common.exceptions.DiscodeitException;
import com.sprint.mission.discodeit.entity.Notification;

import java.util.UUID;

public class NotificationException extends DiscodeitException {
    public NotificationException(ErrorCode errorCode, UUID id) {
        super(Notification.class, errorCode);
        details.put("notificationId", id);
    }
}
