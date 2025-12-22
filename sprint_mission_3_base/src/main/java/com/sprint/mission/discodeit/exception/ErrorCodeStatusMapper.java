package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorCodeStatusMapper {

    public HttpStatus map(ErrorCode code) {
        return switch (code) {
            case USER_NOT_FOUND, CHANNEL_NOT_FOUND, MESSAGE_NOT_FOUND, BINARY_CONTENT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_USER -> HttpStatus.CONFLICT;
            case INVALID_CREDENTIALS -> HttpStatus.UNAUTHORIZED;
            case PRIVATE_CHANNEL_UPDATE -> HttpStatus.FORBIDDEN;
            case VALIDATION_FAILED, BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
