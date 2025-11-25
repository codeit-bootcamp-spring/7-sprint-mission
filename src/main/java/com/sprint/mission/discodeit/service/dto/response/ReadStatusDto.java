package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.ReadStatus;

import java.time.Instant;

public record ReadStatusDto(
        String id,
        Instant lastReadAt,
        String userId,
        String channelId
){
    public static ReadStatusDto from(ReadStatus readStatus){
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getLastReadAt(),
                readStatus.getUserId(),
                readStatus.getChannelId()
        );
    }
}
