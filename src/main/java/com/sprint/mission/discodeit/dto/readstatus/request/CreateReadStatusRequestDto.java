package com.sprint.mission.discodeit.dto.readstatus.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateReadStatusRequestDto {
    private final UUID userId;
    private final UUID channelId;
    private final Instant lastReadAt;
}
