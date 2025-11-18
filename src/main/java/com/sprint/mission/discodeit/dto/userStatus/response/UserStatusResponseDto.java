package com.sprint.mission.discodeit.dto.userStatus.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserStatusResponseDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    Instant lastActiveAt,
    boolean online
) {

  public static UserStatusResponseDto from(UserStatus userStatus) {
    return UserStatusResponseDto.builder()
        .id(userStatus.getId())
        .createdAt(userStatus.getCreateAt())
        .updatedAt(userStatus.getUpdateAt())
        .userId(userStatus.getUserId())
        .lastActiveAt(userStatus.getLastActiveAt())
        .online(userStatus.isOnline().isOnline())
        .build();
  }
}
