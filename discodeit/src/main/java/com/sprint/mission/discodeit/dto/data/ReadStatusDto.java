package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto (
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {
    public static ReadStatusDto from(ReadStatus status) {
        return new ReadStatusDto(
                status.getId(),
                status.getUserId(),
                status.getChannelId(),
                status.getLastReadAt()
        );
    }
}
