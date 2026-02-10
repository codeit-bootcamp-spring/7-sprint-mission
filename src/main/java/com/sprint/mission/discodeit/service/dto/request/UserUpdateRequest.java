package com.sprint.mission.discodeit.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Email
        String newEmail,
        @Size(min=4)
        String newUsername,
        String newPassword
){}
