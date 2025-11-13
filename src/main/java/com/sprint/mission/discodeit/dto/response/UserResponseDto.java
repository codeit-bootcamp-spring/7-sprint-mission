package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserResponseDto(

    UUID id,
    Instant createdAt,
    Instant updateAt,
    String username, // 유저 이름
    String email, // 이메일
    UUID profileId, // 프로필 ID
    Boolean online //온라인 상태
) {

  public static UserResponseDto from(User user, UserStatus status) {
    return new UserResponseDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId(),
        status.isOnline()
    );
  }
}
