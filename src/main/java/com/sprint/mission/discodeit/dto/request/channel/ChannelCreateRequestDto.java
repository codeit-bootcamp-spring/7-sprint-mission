package com.sprint.mission.discodeit.dto.request.channel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelCreateRequestDto {
    private final UUID channelId;
}
