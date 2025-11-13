package com.sprint.mission.discodeit.dto.request.userStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserStatusCreateRequestDto {
   @NotBlank
    private UUID userId;
    private Instant lastOnlineTime;
}
