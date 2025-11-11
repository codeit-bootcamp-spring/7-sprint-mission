package com.sprint.mission.discodeit.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginReq(
        @NotBlank String nickname,
        @NotBlank String password
){}
