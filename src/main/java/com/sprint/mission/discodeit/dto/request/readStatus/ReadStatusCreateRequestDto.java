package com.sprint.mission.discodeit.dto.request.readStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReadStatusCreateRequestDto {
    @NotNull(message = "ReadStatus channel id")
    private UUID channelId;
    @NotNull(message = "ReadStatus user id")
    private UUID userId;
    private Instant lastReadAt;

}
