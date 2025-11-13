package com.sprint.mission.discodeit.entity.dto.readStatusDto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant createAt,
        Instant lastReadAt  // updateAt
){
    public static ReadStatusDto from(ReadStatus readStatus) {
        return ReadStatusDto.builder()
                .id(readStatus.getId())
                .userId(readStatus.getUserId())
                .channelId(readStatus.getChannelId())
                .createAt(readStatus.getCreatedAt())
                .lastReadAt(readStatus.getUpdatedAt())
                .build();
    }
}
