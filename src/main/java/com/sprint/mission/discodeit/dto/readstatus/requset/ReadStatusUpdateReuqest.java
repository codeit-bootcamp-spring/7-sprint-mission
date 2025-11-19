package com.sprint.mission.discodeit.dto.readstatus.requset;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateReuqest(
        Instant newLastReadAt
) {
}
