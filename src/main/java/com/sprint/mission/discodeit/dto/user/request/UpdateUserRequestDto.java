package com.sprint.mission.discodeit.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 수정을 위한 요청 DTO")
public record UpdateUserRequestDto(
        @Schema(description = "이메일", example = "user2@naver.com")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "올바른 이메일 형식이 아닙니다."
        )
        String newEmail,

        @Schema(description = "아이디", example = "user2")
        String newUsername,

        @Schema(description = "비밀번호", example = "user123456")
        @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
        String newPassword
) { }
