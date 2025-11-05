package com.sprint.mission.discodeit.user.dto;

import com.sprint.mission.discodeit.user.User;

import java.util.UUID;

public record AuthUserDTO(
        UUID authUserId,
        String username
) {
    public static AuthUserDTO from(User user) {
        return new AuthUserDTO(
                user.getId(),
                user.getUsername()
        );
    }
}
