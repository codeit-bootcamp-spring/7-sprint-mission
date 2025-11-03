package com.sprint.mission.discodeit.dto.response.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @param slowModeSeconds 슬로우모드 초(s)
 */
public record ChannelResponseDto(
        UUID channelId,
        String channelName,
        String channelDescription,
        Integer slowModeSeconds,
        Instant lastMessage,
        boolean isPrivate,
        List<UUID> userIds,
        ChannelType channelType) {
}
