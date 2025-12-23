package com.sprint.mission.discodeit.dto.entity.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 로그인 요청")
public record UserLoginRequest(
        @Schema(description = "사용자 이름", example = "user1234")
        String username,
        @Schema(description = "비밀번호", example = "pass123456")
        String password
) {
}
