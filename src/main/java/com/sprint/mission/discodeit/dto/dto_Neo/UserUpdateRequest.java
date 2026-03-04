package com.sprint.mission.discodeit.dto.dto_Neo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
    @NotBlank
    String newUsername,

    @Email(message = "Email is invalid")
//    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank
    String newEmail,

    String newPassword) {
}
