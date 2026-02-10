package com.sprint.mission.discodeit.dto.entity.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "사용자 로그인 요청")
public record UserLoginRequest(
        @NotNull(message = "유저 이름은 필수입니다.")
        @Schema(description = "사용자 이름", example = "user1234")
        String username,
        @NotNull(message = "비밀번호는 필수입니다")
        @Schema(description = "비밀번호", example = "pass123456")
        String password
) {
}
