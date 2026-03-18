package com.sprint.mission.discodeit.event.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelDeletedEvent {
    private final ChannelResponseDto channelResponseDto;
}
