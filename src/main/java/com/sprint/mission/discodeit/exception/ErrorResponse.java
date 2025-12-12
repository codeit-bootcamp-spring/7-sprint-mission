package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {

  public static ErrorResponse from(DiscodeitException e) {
    return new ErrorResponse(
        e.getTimestamp(),
        e.getErrorCode().getCode(),
        e.getErrorCode().getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        e.getErrorCode().getStatus().value()
    );
  }

  public static ErrorResponse of(Exception e, String code, HttpStatus status) {
    return new ErrorResponse(
        Instant.now(),
        code,
        e.getMessage(),
        Map.of(),
        e.getClass().getSimpleName(),
        status.value()
    );
  }

}
