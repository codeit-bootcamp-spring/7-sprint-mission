package com.sprint.mission.discodeit.global.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class MessageCreatedEvent {
    private final UUID messageId;
    private final UUID channelId;
    private final UUID senderId;
}
