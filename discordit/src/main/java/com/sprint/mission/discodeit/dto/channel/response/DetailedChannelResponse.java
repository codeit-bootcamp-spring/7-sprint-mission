package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.ChannelScope;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record DetailedChannelResponse(
        UUID id,
        ChannelScope type,
        String name,
        String description,
        Set<UUID> participantIds,
        String lastMessageAt

) {
    public static DetailedChannelResponse toDto(Channel channel, Instant lastMessageAt) {
        return new DetailedChannelResponse(
                channel.getUuid(),
                channel.getScope(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getMembers().stream()
                        .map(User::getUuid)
                        .collect(Collectors.toSet()),
                lastMessageAt.toString()
        );
    }
}
