package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserResponseDto (

    UUID userId,
    String username, // 유저 이름
    String nickName,
    String email, // 이메일
    UUID profileId, // 프로필 ID
    Boolean online, //온라인 상태
    Instant createdAt,
    Instant updateAt
) {
    public static UserResponseDto from(User user, UserStatus status) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getEmail(),
                user.getProfileId(),
                status.isOnline(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
