package com.sprint.mission.discodeit.dto.loginDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder
public record LoginRequest(
        @NotBlank(message = "아이디를 입력해 주세요.")
        String username,
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password) {

}
