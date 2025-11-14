package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;


public record Res_UserStatus( //all private final
    //@NotBlank(message = "userStatusId is mandatory")
    UUID id,
    Instant createdAt,
    Instant updatedAt,

    //@NotBlank(message = "id is mandatory")
    UUID userId,
    Instant lastActiveAt,
    //@NotBlank(message = "isOnline is mandatory")
    boolean isOnline
) {
    public static Res_UserStatus from(UserStatus userStatus) {
        return new Res_UserStatus(
            userStatus.getId(),
            userStatus.getCreatedAt(),
            userStatus.getUpdatedAt(),
            userStatus.getUserId(),
            Instant.now(),
            userStatus.isOnline()
        );
    }
}
