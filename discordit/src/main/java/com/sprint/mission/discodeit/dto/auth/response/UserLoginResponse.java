package com.sprint.mission.discodeit.dto.auth.response;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserLoginResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String password,
        UUID profileId
) {
    public static UserLoginResponse toDto(User user) {
        return new UserLoginResponse(
                user.getUuid(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDisplayName(),
                user.getEmail(),
                user.getPasswd(),
                user.getProfileImage() == null ? null : user.getProfileImage().getUuid()
        );
    }
}
