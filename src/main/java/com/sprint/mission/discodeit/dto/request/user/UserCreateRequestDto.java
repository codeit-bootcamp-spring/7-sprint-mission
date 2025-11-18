package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserCreateRequestDto {
    @NotBlank(message = "User name")
    private String username ;
    @NotBlank(message = "User email")
    private String email;
    @NotBlank(message = "User password")
    private String password;
}
