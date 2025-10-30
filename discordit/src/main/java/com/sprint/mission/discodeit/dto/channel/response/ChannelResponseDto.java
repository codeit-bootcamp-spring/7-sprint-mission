package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.enums.ChannelType;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record ChannelResponseDto(
        String channelName,
        ChannelScope scope,
        ChannelType type,
        String description,
        Set<String> moderators,
        Set<String>memberIds,
        Instant recentMessageTime

) {
    public static ChannelResponseDto toDto(Channel channel, Instant recentTime) {
        return new ChannelResponseDto(
                channel.getChannelName(),
                channel.getScope(),
                channel.getType(),
                channel.getDescription(),
                channel.getModerators().stream()
                        .map(User::getUserId)
                        .collect(Collectors.toSet()),
                channel.getMembers().stream()
                        .map(User::getUserId)
                        .collect(Collectors.toSet()),
                recentTime
        );
    }
}
