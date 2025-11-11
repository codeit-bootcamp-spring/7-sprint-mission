package com.sprint.mission.discodeit.dto.request.readStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReadStatusCreateRequestDto {

    private UUID channelId;
    private UUID userId;
    private Instant lastReadAt;

}
