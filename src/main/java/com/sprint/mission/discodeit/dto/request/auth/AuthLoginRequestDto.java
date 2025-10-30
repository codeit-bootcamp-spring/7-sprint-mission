package com.sprint.mission.discodeit.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequestDto(
        @NotBlank(message = "이름을 확인해주세요.")
        String username,

        @NotBlank(message = "비밀번호를 확인하세요.")
        String password) {
}
