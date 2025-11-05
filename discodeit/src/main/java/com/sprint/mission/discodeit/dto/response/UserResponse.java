package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID profileId;
    private boolean isOnline;

    public static UserResponse from(User user, boolean isOnline) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfileId(),
                isOnline
        );
    }
}
