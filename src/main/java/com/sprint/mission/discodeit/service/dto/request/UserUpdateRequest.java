package com.sprint.mission.discodeit.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Email
        @NotNull
        String newEmail,
        @NotNull
        @Size(min=4)
        String newUsername,
        @NotNull
        String newPassword
){}
