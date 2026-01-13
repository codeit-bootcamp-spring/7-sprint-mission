package com.sprint.mission.discodeit.dto.user;

public record UserUpdateRequestDto(
        String newUserName,
        String newEmail,
        String newPassword
) {
}
