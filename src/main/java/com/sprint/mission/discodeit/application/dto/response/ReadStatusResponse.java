package com.sprint.mission.discodeit.application.dto.response;

import com.sprint.mission.discodeit.domain.ReadStatus;

import java.time.Duration;
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
