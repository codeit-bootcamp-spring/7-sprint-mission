package com.sprint.mission.discodeit.dto.auth.request;

import com.sprint.mission.discodeit.dto.common.Sanitizable;
import jakarta.validation.constraints.NotBlank;

public record UserLoginReq(
    @NotBlank String nickname,
    @NotBlank String password
) implements Sanitizable {

  @Override
  public Object toLoggingDTO() {
    return new UserLoginReq(nickname, "*****");
  }
}
