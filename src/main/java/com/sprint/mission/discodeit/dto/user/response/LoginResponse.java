package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;

public record LoginResponse(
        java.util.UUID userId,
        String username,
        boolean isOnline
) {
    public static LoginResponse from(User user, boolean isOnline) {
        return new LoginResponse(user.getId(), user.getUsername(), isOnline);
    }
}
