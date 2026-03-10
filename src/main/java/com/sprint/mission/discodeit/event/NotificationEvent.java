package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.request.notification.NotificationDto;

public record NotificationEvent(
        NotificationDto notificationDto,
        String eventName
) {
}
