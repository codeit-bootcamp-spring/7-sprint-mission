package com.sprint.mission.discodeit.entity.dto.userDto;

import jakarta.validation.constraints.NotNull;

public record UserUpdateDto(
        String newUserName,
        String newEmail,
        String newPassword
) {
}
