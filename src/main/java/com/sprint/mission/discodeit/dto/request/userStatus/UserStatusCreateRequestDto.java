package com.sprint.mission.discodeit.dto.request.userStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;


public record UserStatusCreateRequestDto(

        @NotNull(message = "UserStatus userId")
        UUID userId,

        Instant lastOnlineTime
) {


}
