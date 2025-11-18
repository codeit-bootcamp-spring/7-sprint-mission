package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Common;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelResponseDto(
        UUID id,
        ChannelType type,
        String name,
        String description,
        List<UUID> participantIds,
        Instant lastMessageAt
) {
    public static ChannelResponseDto from(Channel channel, Instant lastMessageAt) {
        List<UUID> participantIds = channel.getParticipants().stream()
                .map(Common::getId)
                .toList();

        return ChannelResponseDto.builder()
                .id(channel.getId())
                .type(channel.getChannelType())
                .name(channel.getChannelName())
                .description(channel.getDesc())
                .participantIds(participantIds)
                .lastMessageAt(lastMessageAt)
                .build();
    }
}

