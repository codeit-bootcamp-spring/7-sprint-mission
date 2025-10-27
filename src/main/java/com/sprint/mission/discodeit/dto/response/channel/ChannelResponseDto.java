package com.sprint.mission.discodeit.dto.response.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelResponseDto {
    private final UUID channelId;
    private final String channelName;
    private final String channelDescription;
    private final Integer slowModeSeconds; // 슬로우모드 초(s)
    private final Instant lastMessage;
    private final boolean isPrivate;
    private final List<UUID> userIds;
    private final ChannelType channelType;
}
