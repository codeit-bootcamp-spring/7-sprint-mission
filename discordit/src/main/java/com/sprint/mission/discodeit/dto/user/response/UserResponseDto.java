package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.enums.OnlineStatus;

public record UserResponseDto(
        String userId,
        String email,
        String displayName,
        String bio,
        OnlineStatus onlineStatus,
        BinaryContent profileImage
) {
    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getBio(),
                user.getOnlineStatus(),
                user.getProfileImage()
        );
    }
}
