package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
       String newUsername,
        String newEmail,
        String newPassword
) {

}
