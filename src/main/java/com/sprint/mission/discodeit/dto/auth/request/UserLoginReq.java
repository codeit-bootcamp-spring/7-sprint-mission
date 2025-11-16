package com.sprint.mission.discodeit.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginReq(
    @NotBlank String nickname,
    @NotBlank String password
) {

}
