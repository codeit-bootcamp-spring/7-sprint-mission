package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.User;


import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
    Instant createAt,
    Instant updatedAt,
    String email,
    String username,
    UUID profileId,
    Boolean online
){
    public static UserResponse from(User user){
        return new UserResponse(user.getId(),user.getCreatedAt(),user.getUpdatedAt(),user.getEmail(),
                user.getUsername(),user.getProfile(),user.checkOnline());
    }
}
