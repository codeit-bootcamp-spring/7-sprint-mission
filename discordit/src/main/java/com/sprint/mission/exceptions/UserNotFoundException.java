package com.sprint.mission.exceptions;

import com.sprint.mission.entity.User;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String id) {
        super("User not found with id <" + id + ">");
    }

    public UserNotFoundException(User user){
        super("User not found : <" + user.getUuid() + ">");
    }
}
