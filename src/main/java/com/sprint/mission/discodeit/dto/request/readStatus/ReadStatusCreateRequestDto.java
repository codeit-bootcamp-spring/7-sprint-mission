package com.sprint.mission.discodeit.dto.request.readStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;


public record ReadStatusCreateRequestDto(

        @NotNull(message = "ReadStatus channel id")
        UUID channelId,

        @NotNull(message = "ReadStatus user id")
        UUID userId,

        Instant lastReadAt

) {


}
