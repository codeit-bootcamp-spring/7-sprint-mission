package com.sprint.mission.discodeit.event.channel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelCreatedEvent {
    private final UUID channelId;
}
