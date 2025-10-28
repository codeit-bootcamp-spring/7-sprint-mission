package com.sprint.mission.discodeit.entity.status.dto;

import com.sprint.mission.discodeit.entity.status.repository.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public class UserStatusInfoDto {

    private final UUID id;
    private final UUID userId;
    private final Instant createdAt;
    private final Instant lastAccessAt;
    boolean isOnline;

    public UserStatusInfoDto from(UserStatus userStatus) {
        return UserStatusInfoDto.builder()
                .id(userStatus.getId())
                .userId(userStatus.getUserId())
                .createdAt(userStatus.getCreatedAt())
                .lastAccessAt(userStatus.getUpdatedAt())
                .isOnline(userStatus.isOnline())
                .build();
    }
}
