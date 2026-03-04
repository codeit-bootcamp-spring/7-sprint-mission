package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.common.event.RoleUpdatedEvent;

public interface NotificationDispatchService {
    void dispatchMessageCreated(MessageCreatedEvent event);
    void dispatchRoleUpdated(RoleUpdatedEvent event);
}
