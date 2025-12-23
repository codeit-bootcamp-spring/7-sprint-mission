package com.sprint.mission.discodeit.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserDto(
        @NotBlank(message = "username은 필수입니다.")
        String newUsername,

        @NotBlank(message = "username은 필수입니다.")
        @Email
        String newEmail,

        @NotBlank(message = "password는 필수입니다.")
        String newPassword
) {

}
