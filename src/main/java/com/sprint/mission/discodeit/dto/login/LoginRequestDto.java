package com.sprint.mission.discodeit.dto.login;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "username은 필수입니다.")
        String username,

        @NotBlank(message = "password는 필수입니다.")
        String password
) {
}
