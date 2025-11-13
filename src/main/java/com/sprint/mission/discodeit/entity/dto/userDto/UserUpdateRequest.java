package com.sprint.mission.discodeit.entity.dto.userDto;

public record UserUpdateRequest(
        String newUsername,
        String newEmail,
        String newPassword
) {
}
