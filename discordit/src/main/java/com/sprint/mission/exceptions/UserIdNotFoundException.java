package com.sprint.mission.exceptions;

public class UserIdNotFoundException extends RuntimeException {

    public UserIdNotFoundException(String id) {
        super("User not found with id <" + id + ">");
    }
}
