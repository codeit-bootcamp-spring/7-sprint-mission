package com.sprint.mission.discodeit.entity.exception;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException() {
        super();
    }
    public DuplicateUserException(String message) {
        super(message);
    }
}
