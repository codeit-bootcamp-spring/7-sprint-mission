package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse (
        UUID id,
        Instant lastReadAt,
        UUID userId,
        UUID channelId
){
    public static ReadStatusResponse from(ReadStatus readStatus){
        return new ReadStatusResponse(
                readStatus.getId(),
                readStatus.getLastReadAt(),
                readStatus.getUserId(),
                readStatus.getChannelId()
        );
    }
}
