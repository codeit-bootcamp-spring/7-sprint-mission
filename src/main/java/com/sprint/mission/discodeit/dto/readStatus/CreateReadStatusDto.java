package com.sprint.mission.discodeit.dto.readStatus;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class CreateReadStatusDto {
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;
}
