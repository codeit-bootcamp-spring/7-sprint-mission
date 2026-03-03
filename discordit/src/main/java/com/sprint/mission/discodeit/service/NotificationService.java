package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.notification.NotificationDto;
import com.sprint.mission.discodeit.dto.entity.notification.request.NotificationCreateRequest;

public interface NotificationService {
    NotificationDto getByUserId(NotificationCreateRequest request);
}
