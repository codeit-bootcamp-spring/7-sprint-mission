package com.sprint.mission.discodeit.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class MessageCreatedEvent {
    private UUID channelId;
    private UUID senderId;
    private String senderName;
    private String channelName;
    private String content;
}
