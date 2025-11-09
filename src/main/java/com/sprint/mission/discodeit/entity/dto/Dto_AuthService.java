package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record Dto_AuthService( //all private final
    @NotBlank(message = "Username is mandatory")
    String userName,

    @NotBlank(message = "password is mandatory")
    String password
) {
    public static Dto_AuthService from(String userName, String password) {
        return Dto_AuthService.builder()
                .userName(userName)
                .password(password)
                .build();
    }
}
