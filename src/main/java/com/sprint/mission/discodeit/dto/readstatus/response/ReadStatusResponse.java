package com.sprint.mission.discodeit.dto.readstatus.response;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.UUID;

public record ReadStatusResponse(
        UUID readStatusId,
        UUID userId,
        UUID channelId

) {
    public static ReadStatusResponse from(ReadStatus readStatus) {
        return new ReadStatusResponse(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId()
        );
    }
}
