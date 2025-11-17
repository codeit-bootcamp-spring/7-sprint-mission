package com.sprint.mission.discodeit.user.dto;

import com.sprint.mission.discodeit.user.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
    UUID userId,

    String username,

    String email,

    String nickname,

    String phoneNum,

    Instant createdAt,

    Instant updatedAt,

    String password
) {

  public static UserResponseDTO fromEntity(User user) {
    return new UserResponseDTO(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getNickname(),
        user.getPhoneNum(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getPassword()
    );
  }
}
