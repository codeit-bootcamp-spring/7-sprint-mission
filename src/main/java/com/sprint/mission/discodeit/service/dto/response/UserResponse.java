package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        String id,
        String email,
        String username,
        BinaryContentResponse profile,
        Boolean online
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId().toString(), user.getEmail(),
                user.getUsername(), user.getProfile(), user.checkOnline());
    }
}
