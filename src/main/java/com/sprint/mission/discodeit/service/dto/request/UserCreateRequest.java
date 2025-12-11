package com.sprint.mission.discodeit.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @Email
        @NotNull
        String email,

        @NotNull
        @Size(min=4)
        String password,

        @NotNull
        String username
) {}