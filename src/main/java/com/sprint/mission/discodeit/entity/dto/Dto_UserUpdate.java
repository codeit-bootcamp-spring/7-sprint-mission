package com.sprint.mission.discodeit.entity.dto;

public record Dto_UserUpdate(
//        @NotBlank(message = "Username is mandatory")
    String newUsername,
//        @NotBlank(message = "Email is mandatory")
//        @Email(message = "Email is invalid")
//        @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", content = "Email is invalid")
    String newEmail,
//        @NotBlank(message = "Password is mandatory")
    String newPassword) {
}
