package com.sprint.mission.discodeit.exceptions;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String id) {
        super("User not found with id <" + id + ">");
    }

    public UserNotFoundException(User user) {
        super("User not found : \nuuid:" + user.getUuid() + "id: " + user.getUuid());
    }

    public UserNotFoundException(UUID uuid) {
        super("User not found with id <" + uuid + ">");
    }
}
