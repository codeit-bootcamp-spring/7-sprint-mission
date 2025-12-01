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
        .createdAt(readStatus.getCreatedAt())
        .updatedAt(readStatus.getUpdatedAt())
        .userId(readStatus.getUser().getId())
        .channelId(readStatus.getChannel().getId())
        .lastReadAt(readStatus.getLastReadAt())
        .build();
  }
}
