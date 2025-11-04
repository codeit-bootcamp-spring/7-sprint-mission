package com.sprint.mission.discodeit.dto.readstatus.requset;

import java.util.UUID;

public record ReadStatusFindByUserRequest(
        UUID userID
) {
}
