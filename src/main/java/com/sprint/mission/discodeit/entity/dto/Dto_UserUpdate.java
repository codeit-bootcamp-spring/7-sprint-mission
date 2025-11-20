package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotNull;

public record Dto_UserUpdate(
    @NotNull
    String newUsername,
//        @NotBlank(message = "Email is mandatory")
//        @Email(message = "Email is invalid")
//        @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", content = "Email is invalid")
    @NotNull
    String newEmail,
    @NotNull
    String newPassword) {
}
