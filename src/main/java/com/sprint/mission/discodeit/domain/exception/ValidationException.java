package com.sprint.mission.discodeit.domain.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final ErrorType errorType;

    public ValidationException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }


}
