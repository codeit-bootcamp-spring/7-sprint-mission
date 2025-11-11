package com.sprint.mission.discodeit.dto.userstatus.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateUserStatusRequestDto {
    private final Instant newLastActiveAt;
}
