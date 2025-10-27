package com.sprint.mission.discodeit.dto.request.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PublicChannelCreateRequestDto {
    private final String channelName;
    private final String description; // 0: 음성채널, 1: 채팅채널
    private final Integer slowModeSeconds; // 슬로우모드 초(s)
    private final ChannelType channelType;
}
