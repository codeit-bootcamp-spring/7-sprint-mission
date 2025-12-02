package com.sprint.mission.discodeit.service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserStatusDto {
    private UUID id;
    private UUID userId;
    private Instant lastActiveAt;
}
