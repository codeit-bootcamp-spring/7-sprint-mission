package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;


public record Res_UserUpdate( //all private final
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
    public static Res_UserUpdate from(UserStatus userStatus) {
        return new Res_UserUpdate(
            userStatus.getId(),
            userStatus.getCreatedAt(),
            userStatus.getUpdatedAt(),
            userStatus.getUserId(),
            Instant.now(),
            userStatus.isOnline()
        );
    }
}
