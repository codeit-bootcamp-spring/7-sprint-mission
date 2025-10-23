package com.sprint.mission.discodeit.user.domain.exception;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException() {
        super();
    }
    public DuplicateUserException(String message) {
        super(message);
    }
}
