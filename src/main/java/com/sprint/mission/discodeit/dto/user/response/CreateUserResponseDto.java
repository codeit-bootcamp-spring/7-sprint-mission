package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateUserResponseDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    UUID profileId
) {

  public static CreateUserResponseDto from(User user) {
    return CreateUserResponseDto.builder()
        .id(user.getId())
        .createdAt(user.getCreateAt())
        .updatedAt(Instant.now())
        .username(user.getUsername())
        .email(user.getEmail())
        .password(user.getPassword())
        .profileId(user.getProfileId())
        .build();
  }
}
