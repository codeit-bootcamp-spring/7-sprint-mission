package com.sprint.mission.discodeit.dto.request.readstatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReadStatusUpdateRequestDto {
    private final UUID id;
    private final Instant lastReadAt;
}
