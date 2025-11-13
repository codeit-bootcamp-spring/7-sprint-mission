package com.sprint.mission.discodeit.entity.dto.userDto;

public record UserUpdateRequest(
        String newUserName,
        String newEmail,
        String newPassword
) {
}
