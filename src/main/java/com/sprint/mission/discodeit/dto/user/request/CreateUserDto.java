package com.sprint.mission.discodeit.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(
        @NotBlank(message = "username은 필수입니다.")
        String username,

        @NotBlank(message = "password는 필수입니다.")
        String password,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank(message = "휴대전화 번호는 필수입니다.")
        String phoneNumber,

        // 대명사(한줄 자기소개)는 필수가 아님
        // createBinaryContentDto는 필수가 아님.
        String pronoun

) {
}
