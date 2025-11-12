package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청 DTO")
public record LoginRequest(

    @Schema(description = "사용자 아이디", example = "woodyy")
    String username,

    @Schema(description = "비밀번호", example = "woodyy1234")
    String password
) {

}
