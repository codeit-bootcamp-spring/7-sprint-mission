package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Res_ReadStatus( //all private final
    @NotBlank(message = "readStatusId is mandatory")
    UUID readStatusId,
    Instant createdAt,
    Instant updatedAt,

    @NotBlank(message = "userId is mandatory")
    UUID userId,
    @NotBlank(message = "channelId is mandatory")
    UUID channelId
) {
    public static Res_ReadStatus from(ReadStatus readStatus) {
        return Res_ReadStatus.builder()
                .readStatusId(readStatus.getId())
                .createdAt(readStatus.getCreatedAt())
                .updatedAt(readStatus.getUpdatedAt())
                .userId(readStatus.getId())
                .channelId(readStatus.getChannelId())
                .build();
    }
}
