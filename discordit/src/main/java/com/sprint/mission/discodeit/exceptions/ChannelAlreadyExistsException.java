package com.sprint.mission.discodeit.exceptions;

import java.util.UUID;

public class ChannelAlreadyExistsException extends RuntimeException {
    public ChannelAlreadyExistsException(UUID uuid) {
        super("id '" + uuid + "'는 이미 존재하는 아이디입니다.");
    }
    public ChannelAlreadyExistsException(String message) {
        super(message);
    }

}
