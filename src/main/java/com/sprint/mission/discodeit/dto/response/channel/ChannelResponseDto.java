package com.sprint.mission.discodeit.dto.response.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.ArrayList;
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

    public static ChannelResponseDto from(Channel channel, Instant lastMessage) {
        String name = channel.isPrivateChannel() ? null : channel.getChannelName();
        String description = channel.isPrivateChannel() ? null : channel.getChannelDescription();

        List<UUID> members = new ArrayList<>(channel.getMembers().keySet());

        return new ChannelResponseDto(
                channel.getId(),
                name,
                description,
                channel.getSlowModeSeconds(),
                lastMessage,
                channel.isPrivateChannel(),
                members,
                channel.getType()
        );
    }
}
