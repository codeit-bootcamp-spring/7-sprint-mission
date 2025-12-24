package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDto(
        @NotBlank(message = "유저 이름이 필요합니다.")
        @Size(min = 2, max = 30)
        String username,

        @NotBlank(message = "비밀번호가 필요합니다.")
        @Size(min = 4, max = 20)
        String password,

        @NotBlank(message = "email이 필요합니다.")
        @Email
        String email) {
}
