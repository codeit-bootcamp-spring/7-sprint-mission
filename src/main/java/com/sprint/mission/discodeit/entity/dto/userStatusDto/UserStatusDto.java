package com.sprint.mission.discodeit.entity.dto.userStatusDto;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserStatusDto(UUID id, UUID userId, Instant createdAt, Instant lastAccessAt, boolean isOnline) {

    public static UserStatusDto from(UserStatus userStatus) {
        return UserStatusDto.builder()
                .id(userStatus.getId())
                .userId(userStatus.getUserId())
                .createdAt(userStatus.getCreatedAt())
                .lastAccessAt(userStatus.getUpdatedAt())
                .isOnline(userStatus.isOnline())
                .build();
    }
}
