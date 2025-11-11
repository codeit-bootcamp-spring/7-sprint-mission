package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDto(

    @NotBlank(message = "이름은 필수입니다.")
    String username, // 유저 이름

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식으로 작성해주세요.")
    String email, // 이메일

    @NotBlank(message = "비밀번호는 필수입니다.")
    String password // 유저 비밀번호
) {

}

