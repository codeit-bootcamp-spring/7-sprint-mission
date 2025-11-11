package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Res_UserStatus( //all private final
    @NotBlank(message = "userStatusId is mandatory")
    UUID userStatusId,
    Instant createdAt,
    Instant updatedAt,

    @NotBlank(message = "userId is mandatory")
    UUID userId,
    @NotBlank(message = "isOnline is mandatory")
    boolean isOnline
) {
    public static Res_UserStatus from(UserStatus userStatus) {
        return Res_UserStatus.builder()
                .userStatusId(userStatus.getId())
                .createdAt(userStatus.getCreatedAt())
                .updatedAt(userStatus.getUpdatedAt())
                .userId(userStatus.getUserId())
                .isOnline(userStatus.isOnline())
                .build();
    }
}
