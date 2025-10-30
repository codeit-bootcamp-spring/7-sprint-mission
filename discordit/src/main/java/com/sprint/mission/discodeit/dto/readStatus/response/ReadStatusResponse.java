package com.sprint.mission.discodeit.dto.readStatus.response;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse (
        UUID id,
        String userId,
        UUID channelId,
        Instant lastReadAt
){
    public static ReadStatusResponse toDto(ReadStatus readStatus) {
        return new ReadStatusResponse(
                readStatus.getId(),
                readStatus.getUser().getUserId(),
                readStatus.getChannel().getUuid(),
                readStatus.getLastReadAt()
        );
    }

}
