package com.sprint.mission.discodeit.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 수정을 위한 요청 DTO")
public record UpdateUserRequestDto(
        @Schema(description = "이메일", example = "user2@naver.com")
        String newEmail,

        @Schema(description = "아이디", example = "user2")
        String newUsername,

        @Schema(description = "비밀번호", example = "user123456")
        String newPassword
) { }
