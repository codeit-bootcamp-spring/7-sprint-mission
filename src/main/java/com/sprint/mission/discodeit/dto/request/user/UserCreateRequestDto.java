package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


public record UserCreateRequestDto(

        @NotBlank(message = "User name")
        String username,

        @NotBlank(message = "User email")
        String email,

        @NotBlank(message = "User password")
        String password
) {


}
