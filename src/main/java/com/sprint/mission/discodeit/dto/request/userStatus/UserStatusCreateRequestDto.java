package com.sprint.mission.discodeit.dto.request.userStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserStatusCreateRequestDto {
   @NotNull(message = "UserStatus userId")
    private UUID userId;
    private Instant lastOnlineTime;
}
