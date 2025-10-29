package com.sprint.mission.discodeit.entity.status.dto;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusInfoDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant createAt,
        Instant lastReadAt  // updateAt
){
    public static ReadStatusInfoDto from(ReadStatus readStatus) {
        return ReadStatusInfoDto.builder()
                .id(readStatus.getId())
                .userId(readStatus.getUserId())
                .channelId(readStatus.getChannelId())
                .createAt(readStatus.getCreatedAt())
                .lastReadAt(readStatus.getUpdatedAt())
                .build();
    }
}
