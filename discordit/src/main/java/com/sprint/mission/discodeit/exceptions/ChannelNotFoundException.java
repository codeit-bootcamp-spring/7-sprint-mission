package com.sprint.mission.discodeit.exceptions;

import java.util.UUID;

public class ChannelNotFoundException extends RuntimeException {
    public ChannelNotFoundException(UUID uuid) {
        super("잘못된 UUID입니다.");
    }
}
