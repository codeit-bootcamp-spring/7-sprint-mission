package com.sprint.mission.discodeit.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponseV2(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String password,
        String email,
        UUID profileId,
        boolean online
) {
    public static UserResponseV2 toDto(User user) {
        UUID profileId = user.getProfileImage() != null ? user.getProfileImage().getUuid() : null;
        return new UserResponseV2(
                user.getUuid(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDisplayName(),
                user.getPasswd(),
                user.getEmail(),
                profileId,
                user.getOnlineStatus() != OnlineStatus.OFFLINE
        );
    }
}
