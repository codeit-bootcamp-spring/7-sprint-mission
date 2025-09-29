package com.sprint.mission.exceptions;

import java.util.UUID;

public class ChannelIdNotFoundException extends RuntimeException {
    public ChannelIdNotFoundException(UUID uuid) {
        super("잘못된 UUID입니다.");
    }
}
