package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.notification.NotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDto> getAllByUserId(UUID userId);

    List<NotificationDto> getAll();

    void confirm(UUID requestedUserId, UUID id);
}
