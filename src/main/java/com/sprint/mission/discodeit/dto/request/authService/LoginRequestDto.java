package com.sprint.mission.discodeit.dto.request.authService;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;


public record LoginRequestDto(
        @NotBlank(message = "유저 이름은 필수값입니다")
        String username,

        @NotBlank(message = "비밀번호는 필수값입니다")
        String password
) {
}
