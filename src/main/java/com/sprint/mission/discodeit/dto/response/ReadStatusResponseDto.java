package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class ReadStatusResponseDto {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public static ReadStatusResponseDto from(ReadStatus readStatus){
        return ReadStatusResponseDto.builder()
                .id(readStatus.getId())
                .userId(readStatus.getUser().getId())
                .channelId(readStatus.getChannel().getId())
                .lastReadAt(readStatus.getReadLastTime())
                .createdAt(readStatus.getCreatedAt())
                .updatedAt(readStatus.getUpdatedAt())
                .build();
    }
}
