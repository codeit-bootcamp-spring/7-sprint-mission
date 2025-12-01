package com.sprint.mission.discodeit.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserCreateRequest(
        @NotBlank(message = "이름은 공백일 수 없습니다.")
        String username,
        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        String email,
        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        String password
) {
}
