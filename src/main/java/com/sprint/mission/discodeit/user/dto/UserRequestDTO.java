package com.sprint.mission.discodeit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public record UserRequestDTO(
        @NotNull(message = "아이디는 필수입니다.")
        @NotBlank
        String username,

        @NotNull(message = "비밀번호는 필수입니다.")
        @NotBlank
        @Pattern(
                regexp = "^(?!.*\\s)(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+-=]).{10,14}$",
                message = "비밀번호는 10~14자리여야 하며, 공백 없이 영어, 숫자, 특수문자(!@#$%^&*()_+-=)를 각각 1개 이상 포함해야 합니다."
        )
        String password,

        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        String nickname,

        String phoneNum
) {

}
