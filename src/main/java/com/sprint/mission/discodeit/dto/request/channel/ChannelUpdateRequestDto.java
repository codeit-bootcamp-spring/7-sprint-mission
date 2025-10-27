package com.sprint.mission.discodeit.dto.request.channel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelUpdateRequestDto {
    private final UUID channelId;
    private final String channelName;
    private final String channelDescription;
    private final Integer slowModeSeconds;
}
