package com.sprint.mission.discodeit.dto.request.readStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;


public record ReadStatusCreateRequestDto(

        @NotNull(message = "ReadStatus 채널 id는 필수값입니다")
        UUID channelId,

        @NotNull(message = "ReadStatus 유저 id는 필수값입니다")
        UUID userId,

        Instant lastReadAt

) {


}
