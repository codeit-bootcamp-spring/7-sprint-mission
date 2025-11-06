package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Dto_ReadStatus( //all private final
//        UUID readStatusID,
//        Instant createdAt,
//        Instant updatedAt // 유닉스 타임스탬프
        @NotBlank(message = "userId is mandatory")
        UUID userId,
        @NotBlank(message = "channelId is mandatory")
        UUID channelId
){
    public static Dto_ReadStatus from(UUID userId, UUID channelId) {
        return Dto_ReadStatus.builder()
                .userId(userId)
                .channelId(channelId)
                .build();
    }
}