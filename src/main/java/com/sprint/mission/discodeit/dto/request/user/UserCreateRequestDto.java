package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserCreateRequestDto {
    @NotBlank
    private String username ;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
