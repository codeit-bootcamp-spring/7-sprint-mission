package com.sprint.mission.discodeit.dto.readStatus.response;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse (
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
){
    public static ReadStatusResponse toDto(ReadStatus readStatus) {
        return new ReadStatusResponse(
                readStatus.getUuid(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt(),
                readStatus.getUser().getUuid(),
                readStatus.getChannel().getUuid(),
                readStatus.getLastReadAt()
        );
    }

}
