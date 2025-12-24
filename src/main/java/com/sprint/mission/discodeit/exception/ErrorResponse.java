package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    public Instant timestamp;
    public String code;
    public String message;
    public Map<String, Object> details;
    public String exceptionType;
    public int status; // HTTP 상태 코드

    public ErrorResponse(DiscodeitException e, String exceptionType, int status) {
        this.timestamp = e.getTimestamp();
        this.code = e.getErrorCode().toString();
        this.message = e.getMessage();
        this.details = e.getDetails();
        this.exceptionType = exceptionType;
        this.status = status;
    }
}
