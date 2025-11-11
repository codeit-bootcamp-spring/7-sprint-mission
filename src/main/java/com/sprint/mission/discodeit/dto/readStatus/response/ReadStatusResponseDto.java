package com.sprint.mission.discodeit.dto.readStatus.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusResponseDto(
        UUID readStatusId,
        Instant createdAt,
        Instant updateAt,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
    public static ReadStatusResponseDto from(ReadStatus readStatus) {
        return ReadStatusResponseDto.builder()
                .readStatusId(readStatus.getId())
                .createdAt(readStatus.getCreateAt())
                .updateAt(readStatus.getUpdateAt())
                .userId(readStatus.getUserId())
                .channelId(readStatus.getChannelId())
                .lastReadAt(readStatus.getLastReadAt())
                .build();
    }
}
