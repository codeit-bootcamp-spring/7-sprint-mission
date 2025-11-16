package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "사용자 생성 요청")
public record UserCreateRequest(
        @Schema(description = "사용자 ID", example = "user1234")
        @NotNull(message = "id는 필수입니다.")
        String id,

        @Schema(description = "비밀번호", example = "pass123456")
        @NotNull(message = "비밀번호는 필수입니다.")
        String passwd,

        @Schema(description = "이메일", example = "user@example.com")
        @Email(message = "올바른 형식의 이메일을 입력해주세요.")
        @NotNull(message = "이메일은 필수입니다.")
        String email,

        @Schema(description = "표시 이름", example = "홍길동")
        @NotNull(message = "닉네임은 필수입니다.")
        String displayName,

        @Schema(description = "프로필 이미지 ID (선택사항)", example = "123e4567-e89b-12d3-a456-426614174000", nullable = true)
        @Nullable
        UUID profileImageId) {
}
