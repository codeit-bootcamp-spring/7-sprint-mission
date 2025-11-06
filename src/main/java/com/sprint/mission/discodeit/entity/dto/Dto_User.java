package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder
public record Dto_User( //all private final
        @NotBlank(message = "Username is mandatory")
        String username,
        @NotBlank(message = "Password is mandatory")
        String password,
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email is invalid")
//        @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email is invalid")
        String email
        ) {
    public static Dto_User from(String userName, String password, String eMail) {
        return Dto_User.builder()
                                .username(userName)
                                .password(password)
                                .email(eMail)
                                .build();
    }
}
