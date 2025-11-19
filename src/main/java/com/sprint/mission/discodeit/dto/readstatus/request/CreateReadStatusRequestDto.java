package com.sprint.mission.discodeit.dto.readstatus.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateReadStatusRequestDto {
    UUID userId;
    UUID channelId;
    Instant lastReadAt;
}
