package com.sprint.mission.discodeit.config.exception;

import java.util.NoSuchElementException;
import java.util.UUID;

public class ChannelNotFoundException extends NoSuchElementException {
    public ChannelNotFoundException(UUID channelId) {
        super("존재하지 않거나 삭제된 채널입니다. ID: " + channelId);
    }
    public ChannelNotFoundException(String message) {
        super(message);
    }
}