package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.enums.OnlineStatus;

public record UserResponse(
        String userId,
        String email,
        String displayName,
        String bio,
        OnlineStatus onlineStatus,
        BinaryContent profileImage
) {
    public static UserResponse toDto(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getBio(),
                user.getOnlineStatus(),
                user.getProfileImage()
        );
    }
}
