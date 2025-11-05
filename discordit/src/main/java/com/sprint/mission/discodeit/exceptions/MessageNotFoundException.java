package com.sprint.mission.discodeit.exceptions;

import java.util.UUID;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(UUID id) {
        super("메세지가 존재하지 않습니다 : " + id);
    }

    public MessageNotFoundException(String message) {
        super(message);
    }
}
