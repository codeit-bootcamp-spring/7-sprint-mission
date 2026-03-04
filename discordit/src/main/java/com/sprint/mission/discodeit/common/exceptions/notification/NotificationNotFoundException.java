package com.sprint.mission.discodeit.common.exceptions.notification;

import com.sprint.mission.discodeit.common.enums.ErrorCode;

import java.util.UUID;

public class NotificationNotFoundException extends NotificationException {
    public NotificationNotFoundException(UUID id) {
        super(ErrorCode.NOT_FOUND, id);
    }
}
