package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class LoginNotMatchException extends UserException {

  public LoginNotMatchException() {
    super(ErrorCode.LOGIN_NOT_MATCH);
  }

}
