package com.sprint.mission.discodeit.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthLoginRequestDto {
    @NotBlank(message = "이름을 확인해주세요.")
    private final String username;

    @NotBlank(message = "비밀번호를 확인해세요.")
    private final String password;
}
