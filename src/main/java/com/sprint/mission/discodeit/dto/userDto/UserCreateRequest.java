package com.sprint.mission.discodeit.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserCreateRequest(
        @NotBlank(message = "이름은 공백일 수 없습니다.")
        String username,

        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$",
                message = "비밀번호 형식이 올바르지 않습니다.\n비밀번호는 8자 이상(대소문자, 숫자, 특수문자 모두 포함), 15자 이하입니다.")
        String password
) {
}
