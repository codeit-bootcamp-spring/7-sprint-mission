package com.sprint.mission.discodeit.service.dto.request;

import java.util.UUID;

public record ReadStatusUpdateRequest (
        boolean newNotificationEnabled
) {
}
