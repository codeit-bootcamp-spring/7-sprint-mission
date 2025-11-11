package com.sprint.mission.discodeit.application.dto.response;

import com.sprint.mission.discodeit.domain.User;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
        UUID userId,
        Instant lastActiveAt,
        boolean online
) {
    public static UserStatusResponse from(User user){
        return new UserStatusResponse(user.getId(), user.getUserStatus().getLastAt(),user.checkOnline());
    }
}
