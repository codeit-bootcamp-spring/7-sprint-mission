package com.sprint.mission.discodeit.config.exception;

import java.util.NoSuchElementException;
import java.util.UUID;

public class UserNotFoundException extends NoSuchElementException {
    public UserNotFoundException(UUID userId) {
        super("존재하지 않거나 탈퇴한 사용자입니다. ID: " + userId);
    }
    public UserNotFoundException(String message) { // 메시지 직접 지정 가능
        super(message);
    }
}


