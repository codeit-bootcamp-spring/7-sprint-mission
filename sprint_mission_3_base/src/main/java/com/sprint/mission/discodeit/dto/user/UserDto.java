package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        boolean online,          // ✅ 최근 5분 이내 활동 여부
        UUID profileImageId
) {
    public static record UserStatusUpdateRequest(
            UUID id,
            Instant lastSeenAt
    ) {}
}
