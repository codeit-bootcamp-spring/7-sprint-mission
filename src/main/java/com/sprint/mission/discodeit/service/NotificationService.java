package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.event.MessageCreatedEvent;

public interface NotificationService {
    void createForMessageCreated (MessageCreatedEvent event);
}
