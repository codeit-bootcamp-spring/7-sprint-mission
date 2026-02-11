package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(
        @Size(min = 2, max = 30)
        String newUsername,

        @Email
        String newEmail,

        @Size(min = 7, max = 50)
        String newPassword) {
}
