package com.sprint.mission.discodeit.dto.request.readStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReadStatusCreateRequestDto {
    @NotBlank
    private UUID channelId;
    @NotBlank
    private UUID userId;
    private Instant lastReadAt;

}
