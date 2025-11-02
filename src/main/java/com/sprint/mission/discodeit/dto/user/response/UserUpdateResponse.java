package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserUpdateResponse(
        UUID userId,                 // 수정할 대상
        String newUsername,          // null이면 변경 안 함
        String newEmail,
        String newPassword,
        String newNickname,
        UUID newProfileImage

){
    public static UserUpdateResponse from(UUID uuid,User user) {
        return new UserUpdateResponse(
                user.getId(),
                user.getUserName(),
                user.getUserEmail(),
                user.getPassword(),
                user.getUserNickname(),
                user.getProfileID()
        );
    }
}
