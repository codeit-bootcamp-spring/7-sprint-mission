package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelVisibility;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Builder
public record ChannelDto(
        UUID id,
        ChannelVisibility type,
        String name,
        String description,
        List<UUID> participantIds,
        Instant lastMessageAt
) {
    public static ChannelDto from(Channel channel, Instant lastMessageAt) {
        return ChannelDto.builder()
                .id(channel.getId())
                .type(channel.getVisibility())
                .name(channel.getChannelName())
                .description(channel.getDescription())
                .participantIds(channel.getMemberIds())
                .lastMessageAt(lastMessageAt)
                .build();
    }
}
