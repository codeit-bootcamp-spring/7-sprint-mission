package com.sprint.mission.discodeit.dto.request.authService;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank
    String username;
    @NotBlank
    String password;
}
