package com.sprint.mission.discodeit.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "사용자 생성을 위한 요청 DTO")
public class CreateUserRequestDto {
    @Schema(description = "이메일", example = "user1@naver.com")
    @NotBlank(message = "이름은 필수입니다.")
    private final String email;

    @Schema(description = "아이디", example = "user1")
    @NotBlank(message = "아이디는 필수입니다.")
    private final String username;

    @Schema(description = "비밀번호", example = "user12345")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private final String password;
}
