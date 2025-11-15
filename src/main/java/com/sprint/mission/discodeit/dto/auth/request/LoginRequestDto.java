package com.sprint.mission.discodeit.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    String password;
}
