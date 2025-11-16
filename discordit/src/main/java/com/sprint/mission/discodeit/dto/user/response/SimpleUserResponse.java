package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.time.Instant;
import java.util.UUID;

public record SimpleUserResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        boolean online
) {
    public static SimpleUserResponse toDto(User user) {
        UUID profileId = user.getProfileImage() != null ? user.getProfileImage().getUuid() : null;
        return new SimpleUserResponse(
                user.getUuid(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDisplayName(),
                user.getEmail(),
                profileId,
                user.getOnlineStatus() != OnlineStatus.OFFLINE
        );
    }

    public static SimpleUserResponse fromUserResponseV2(UserResponseV2 dto) {
        return new SimpleUserResponse(
                dto.id(),
                dto.createdAt(),
                dto.updatedAt(),
                dto.username(),
                dto.email(),
                dto.profileId(),
                dto.online()
        );
    }
}
