package com.sprint.mission.discodeit.dto.readstatus.request;

import java.time.Instant;
import java.util.UUID;

public record CreateReadStatusRequestDto(
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) { }
