package com.sprint.mission.discodeit.dto.readStatus;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UpdateReadStatusDto {
    private UUID readStatusId;
    private Instant lastReadAt;
}
