package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
       String newUsername,

        @Email(message = "이메일 형식이 아닙니다")
        String newEmail,
        String newPassword
) {

}
