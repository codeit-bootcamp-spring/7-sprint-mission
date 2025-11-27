package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.dto.common.Sanitizable;
import jakarta.validation.constraints.Pattern;

public record UserInfoReq(
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
    String email,
    @Pattern(regexp = "^[\\w가-힣]{2,}$")
    String nickname,
    @Pattern(regexp = "^.{6,}$")
    String password
) implements Sanitizable {

  @Override
  public Object toLoggingDTO() {
    return new UserInfoReq(email, nickname, "*****");
  }
}
