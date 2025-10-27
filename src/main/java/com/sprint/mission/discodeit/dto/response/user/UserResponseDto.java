package com.sprint.mission.discodeit.dto.response.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {
    private final UUID id;
    private final String username;
    private final String email;
    private final UUID profileId;
    private final boolean userState;
    private final Instant createdAt;
    private final Instant updatedAt;
}
