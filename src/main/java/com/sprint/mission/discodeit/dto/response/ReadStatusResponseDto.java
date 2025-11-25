package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponseDto(

    UUID id,
    UUID userId, //유저 ID
    UUID channelId, //채널 ID
    Instant lastReadAt
) {

}
