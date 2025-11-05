package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserCreateResponse(
        UUID userId,
        String username,
        String userNickname,
        UUID profileID
) {
    public static UserCreateResponse from(User user) {
        return new UserCreateResponse(
                user.getId(),
                user.getUserName(),
                user.getUserNickname(),
                user.getProfileID()
        );
    }
}
