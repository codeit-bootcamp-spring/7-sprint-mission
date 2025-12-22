package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


public record UserCreateRequestDto(

        @NotBlank(message = "유저 이름은 필수값입니다")
        String username,

        @Email(message = "이메일 형식이 아닙니다")
        @NotBlank(message = "유저 이메일은 필수값입니다")
        String email,

        @NotBlank(message = "유저 비밀번호는 필수값입니다")
        String password
) {


}
