package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Dto_ReadStatus(
//        UUID readStatusID,
//        Instant createdAt,
//        Instant updatedAt // 유닉스 타임스탬프

        UUID userId,
        UUID channelId
){
    public static Dto_ReadStatus from(UUID userId, UUID channelId) {
        return Dto_ReadStatus.builder()
                .userId(userId)
                .channelId(channelId)
                .build();
    }
}