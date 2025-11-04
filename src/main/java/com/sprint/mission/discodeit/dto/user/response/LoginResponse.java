package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;

public record LoginResponse(
        java.util.UUID userId,
        String username,
        String userNickname
) {
    public static LoginResponse from(User user) {
        return new LoginResponse(user.getId(), user.getUserName(), user.getUserNickname());
    }
}
