package com.sprint.mission.discodeit.dto.request.readstatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReadStatusCreateRequestDto {
    private final UUID userId;
    private final UUID channelId;
    private final Instant lastReadAt;
}
