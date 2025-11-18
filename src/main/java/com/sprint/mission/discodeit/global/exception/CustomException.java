package com.sprint.mission.discodeit.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public CustomException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.errorCode = errorCode;
  }

  @Override
  public String getMessage() {
    return String.format("[%s] %s", errorCode.getCode(), errorCode.getMessage());
  }

}
