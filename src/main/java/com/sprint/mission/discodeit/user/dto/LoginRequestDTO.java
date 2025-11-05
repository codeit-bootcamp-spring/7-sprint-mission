package com.sprint.mission.discodeit.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
        @NotNull(message = "아이디가 없습니다.")
        @NotBlank(message = "아이디가 없습니다.")
        String username,
        @NotNull(message = "비밀번호가 없습니다.")
        @NotBlank(message = "비밀번호가 없습니다.")
        String password
) {
}
