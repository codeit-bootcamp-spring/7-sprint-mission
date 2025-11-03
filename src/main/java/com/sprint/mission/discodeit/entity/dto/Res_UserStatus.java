package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Res_UserStatus(
        UUID id,
        Instant createdAt,
        Instant updatedAt,

        UUID userId,
        boolean isOnline
        ) {
    public static Res_UserStatus from(UserStatus userStatus) {
        return Res_UserStatus.builder()
                .id(userStatus.getId())
                .createdAt(userStatus.getCreatedAt())
                .updatedAt(userStatus.getUpdatedAt())
                .userId(userStatus.getUserId())
                .isOnline(userStatus.isOnline())
                .build();
    }
}
