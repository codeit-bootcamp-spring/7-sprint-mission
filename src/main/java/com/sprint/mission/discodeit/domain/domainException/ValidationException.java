package com.sprint.mission.discodeit.domain.domainException;

public class ValidationException extends RuntimeException {

    private final ErrorType errorType;

    public ValidationException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
