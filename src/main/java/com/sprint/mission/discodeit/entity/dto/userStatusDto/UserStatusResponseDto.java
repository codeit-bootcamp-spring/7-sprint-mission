package com.sprint.mission.discodeit.entity.dto.userStatusDto;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserStatusResponseDto(UUID id, UUID userId, Instant createdAt, Instant lastAccessAt, boolean isOnline) {

    public static UserStatusResponseDto from(UserStatus userStatus) {
        return UserStatusResponseDto.builder()
                .id(userStatus.getId())
                .userId(userStatus.getUserId())
                .createdAt(userStatus.getCreatedAt())
                .lastAccessAt(userStatus.getUpdatedAt())
                .isOnline(userStatus.isOnline())
                .build();
    }
}
