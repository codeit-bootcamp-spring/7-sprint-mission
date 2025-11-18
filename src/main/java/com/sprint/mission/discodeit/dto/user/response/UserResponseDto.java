package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.UserStatusType;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponseDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId,
    Boolean online
) {

  public static UserResponseDto from(User user, UserStatusType userStatusType) {
    return UserResponseDto.builder()
        .id(user.getId())
        .createdAt(user.getCreateAt())
        .updatedAt(Instant.now())
        .username(user.getUsername())
        .email(user.getEmail())
        .profileId(user.getProfileId())
        .online(userStatusType.isOnline())
        .build();
  }
}
