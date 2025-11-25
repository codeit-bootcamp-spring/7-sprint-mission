package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.ReadStatus;

import java.time.Instant;

public record ReadStatusDto(
        String id,
        Instant lastReadAt,
        String userId,
        String channelId
){

}
