package com.sprint.mission.discodeit.dto.response.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        String name,
        String description,
        Integer slowModeSeconds,
        Instant lastMessageAt,
        boolean isPrivate,
        List<UUID> participantIds,
        ChannelType type) {

    public static ChannelResponseDto from(Channel channel, Instant lastMessageAt) {
        String name = channel.isPrivateChannel() ? null : channel.getChannelName();
        String description = channel.isPrivateChannel() ? null : channel.getChannelDescription();

        List<UUID> members = new ArrayList<>(channel.getMembers().keySet());

        return new ChannelResponseDto(
                channel.getId(),
                name,
                description,
                channel.getSlowModeSeconds(),
                lastMessageAt,
                channel.isPrivateChannel(),
                members,
                channel.getType()
        );
    }
}
