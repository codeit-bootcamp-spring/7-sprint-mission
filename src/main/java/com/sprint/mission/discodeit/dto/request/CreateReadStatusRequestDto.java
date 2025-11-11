package com.sprint.mission.discodeit.dto.request;

import java.time.Instant;
import java.util.UUID;

public record CreateReadStatusRequestDto (

    UUID userId, //유저 ID
    Instant lastReadAt
) {}
