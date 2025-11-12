package com.sprint.mission.discodeit.dto.readStatus.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusResponseDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

  public static ReadStatusResponseDto from(ReadStatus readStatus) {
    return ReadStatusResponseDto.builder()
        .id(readStatus.getId())
        .createdAt(readStatus.getCreateAt())
        .updatedAt(readStatus.getUpdateAt())
        .userId(readStatus.getUserId())
        .channelId(readStatus.getChannelId())
        .lastReadAt(readStatus.getLastReadAt())
        .build();
  }
}
