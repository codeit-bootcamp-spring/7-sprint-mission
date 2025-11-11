package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserUserStatusPatchResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        Instant lastActiveAt,
        boolean online
) {
    public static UserUserStatusPatchResponseDto from(UserStatus userStatus){
        return UserUserStatusPatchResponseDto.builder()
                .id(userStatus.getId())
                .createdAt(userStatus.getCreatedAt())
                .updatedAt(userStatus.getUpdatedAt())
                .userId(userStatus.getUserId())
                .lastActiveAt(userStatus.getLastOnlineTime())
                .online(userStatus.isUserOnline())
                .build();
    }
}
