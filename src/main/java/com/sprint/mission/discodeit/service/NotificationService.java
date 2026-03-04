package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatusDto.NotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    List<NotificationDto> findAllNotifications(UUID userId);

    void deleteNotification(UUID id, UUID userId);
}
