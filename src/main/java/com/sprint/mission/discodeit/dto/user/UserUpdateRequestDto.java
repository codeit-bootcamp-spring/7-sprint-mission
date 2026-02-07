package com.sprint.mission.discodeit.dto.user;

public record UserUpdateRequestDto(
        String newUsername,
        String newEmail,
        String newPassword
) {
}
