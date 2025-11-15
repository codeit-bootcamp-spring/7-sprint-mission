package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelVisibility;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ChannelDto {
    private final UUID id;
    private final ChannelVisibility type;
    private final String name;
    private final String description;
    private final List<UUID> participantIds;
    private final Instant lastMessageAt;

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
