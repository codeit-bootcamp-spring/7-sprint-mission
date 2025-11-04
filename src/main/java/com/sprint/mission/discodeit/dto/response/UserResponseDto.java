package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public record UserResponseDto (

    UUID userId,
    String username, // 유저 이름
    String nickName, // 유저 닉네임
    String email, // 이메일
    UUID profileId, // 프로필 ID
    boolean isOnline //온라인 상태
) {
    public static UserResponseDto from(User user, UserStatus status) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getEmail(),
                user.getProfileId(),
                status.isOnline()
        );
    }
}
