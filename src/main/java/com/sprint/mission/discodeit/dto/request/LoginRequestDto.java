package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto (

    @NotBlank (message = "이름은 필수입니다.")
    String username,

    @NotBlank (message = "비밀번호는 필수입니다.")
    String password
) {}
