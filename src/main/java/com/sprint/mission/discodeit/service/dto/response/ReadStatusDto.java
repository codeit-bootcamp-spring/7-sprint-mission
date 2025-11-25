package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
        UUID id,
        Instant lastReadAt,
        UUID userId,
        UUID channelId
){

}
