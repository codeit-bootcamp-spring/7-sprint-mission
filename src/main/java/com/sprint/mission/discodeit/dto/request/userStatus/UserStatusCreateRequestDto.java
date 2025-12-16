package com.sprint.mission.discodeit.dto.request.userStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;


public record UserStatusCreateRequestDto(

        @NotNull(message = "유저 id는 필수값입니다")
        UUID userId,

        Instant lastOnlineTime
) {


}
