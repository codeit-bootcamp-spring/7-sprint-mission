package com.sprint.mission.discodeit.entity.dto.userDto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {

    public static UserDto from(User user, boolean isOnline) {
        return UserDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileId(user.getProfileId())
                .online(isOnline)
                .build();
    }
}
