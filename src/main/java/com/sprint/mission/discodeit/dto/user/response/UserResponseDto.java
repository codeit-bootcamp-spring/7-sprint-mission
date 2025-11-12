package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@ToString
public class UserResponseDto {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String username;
    private final String email;
    private final UUID profileId;
    private final boolean online;

    public static UserResponseDto from(User user, boolean online) {
        return UserResponseDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileId(user.getProfileId())
                .online(online)
                .build();
    }
}
