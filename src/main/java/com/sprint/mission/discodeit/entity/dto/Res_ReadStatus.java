package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Res_ReadStatus(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        UUID channelId
) {
    public static Res_ReadStatus from(ReadStatus readStatus) {
        return Res_ReadStatus.builder()
                .id(readStatus.getId())
                .createdAt(readStatus.getCreatedAt())
                .updatedAt(readStatus.getUpdatedAt())
                .userId(readStatus.getId())
                .channelId(readStatus.getChannelId())
                .build();
    }
}
