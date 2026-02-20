package com.sprint.mission.discodeit.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class MessageCreatedEvent {

    private final String content;
    private final UUID authorId;
    private final UUID channelId;


}
