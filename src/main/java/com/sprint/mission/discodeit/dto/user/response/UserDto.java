package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {
    public static UserDto from(User user, UserStatus status) {
        return new UserDto(
               user.getId(),
               user.getCreatedAt(),
               user.getUpdatedAt(),
                user.getUserName(),
               user.getUserEmail(),
               user.getProfileID(),
               status.isOnline()
        );
    }
}
