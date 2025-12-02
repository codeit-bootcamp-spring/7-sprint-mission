package com.sprint.mission.discodeit.dto.userstatus.request;

import java.time.Instant;

public record UpdateUserStatusRequestDto(
        Instant newLastActiveAt
) { }
