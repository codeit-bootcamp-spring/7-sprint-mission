package com.sprint.mission.discodeit.dto.response.readstatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReadStatusResponseDto {
    private final UUID id;
    private final UUID userId;
    private final UUID channelId;
    private final Instant lastReadAt;
}
