package com.sprint.mission.discodeit.dto.userStatus.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserStatusResponseDto(
    UUID id,
    Instant createdAt,
    Instant updateAt,
    UUID userId,
    Instant loginAt
) {

  public static UserStatusResponseDto from(UserStatus userStatus) {
    return UserStatusResponseDto.builder()
        .id(userStatus.getId())
        .createdAt(userStatus.getCreateAt())
        .updateAt(userStatus.getUpdateAt())
        .userId(userStatus.getUserId())
        .loginAt(userStatus.getLoginAt())
        .build();
  }
}
