package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public record UserStatusDto(
        UUID id,
        UUID userId,
        boolean online,
        String lastActiveAt
) {
    public static UserStatusDto from(UserStatus status) {
        return new UserStatusDto(
                status.getId(),
                status.getUser().getId(),
                status.isOnline(),
                status.getLastActiveAt().toString()
        );
    }
}
