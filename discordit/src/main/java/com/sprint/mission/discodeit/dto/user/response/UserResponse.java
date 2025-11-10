package com.sprint.mission.discodeit.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String userId,
        String email,
        @JsonProperty("username")
        String displayName,
        String bio,
        UUID profileId,
        OnlineStatus onlineStatus,
        Boolean online
) {
    public static UserResponse toDto(User user) {
        UUID profileId = user.getProfileImage() != null ? user.getProfileImage().getId() : null;
        return new UserResponse(
                user.getUuid(),
                user.getUserId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getBio(),
                profileId,
                user.getOnlineStatus(),
                user.getOnlineStatus() == OnlineStatus.ONLINE
        );
    }
}
