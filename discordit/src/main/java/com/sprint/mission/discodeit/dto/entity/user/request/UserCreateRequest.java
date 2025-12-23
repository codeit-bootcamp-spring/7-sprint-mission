package com.sprint.mission.discodeit.dto.entity.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "사용자 생성 요청")
public record UserCreateRequest(
        @Schema(description = "표시 이름", example = "홍길동")
        @NotNull(message = "닉네임은 필수입니다.")
        String username,

        @Schema(description = "이메일", example = "user@example.com")
        @Email(message = "올바른 형식의 이메일을 입력해주세요.")
        @NotNull(message = "이메일은 필수입니다.")
        String email,

        @Schema(description = "비밀번호", example = "pass123456")
        @NotNull(message = "비밀번호는 필수입니다.")
        String password
) {
}
