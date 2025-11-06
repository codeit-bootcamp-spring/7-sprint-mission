package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
public record Dto_UserStatus( //all private final
        @NotBlank(message = "userStatusId is mandatory")
        UUID userStatusId
//        Instant createdAt,
//        Instant updatedAt, // 유닉스 타임스탬프

//        UUID userId,
//        boolean online
) {
    public static Dto_UserStatus from(UUID userStatusID) {
        return Dto_UserStatus.builder()
                .userStatusId(userStatusID)
//                .online(online)
                .build();
    }
}
