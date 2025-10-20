package com.sprint.mission.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String id) {
        super("id '" + id + "'는 이미 존재하는 아이디입니다.");
    }
}
