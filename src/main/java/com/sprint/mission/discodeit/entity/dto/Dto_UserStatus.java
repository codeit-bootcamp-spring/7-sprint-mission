package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
// 🚫 편의상 변경. CamelCase 위반 -> 추후 수정 할꺼예용~
public record Dto_UserStatus(
        UUID id
//        Instant createdAt,
//        Instant updatedAt, // 유닉스 타임스탬프

//        UUID userId,
//        boolean isOnline
) {
    public static Dto_UserStatus from(UUID userStatusID) {
        return Dto_UserStatus.builder()
                .id(userStatusID)
//                .isOnline(isOnline)
                .build();
    }
}
