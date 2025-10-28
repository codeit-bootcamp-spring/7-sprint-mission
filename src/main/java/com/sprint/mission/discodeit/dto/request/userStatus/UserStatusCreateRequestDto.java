package com.sprint.mission.discodeit.dto.request.userStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserStatusCreateRequestDto {
    private UUID userId;
    private Instant lastOnlineTime;
}
