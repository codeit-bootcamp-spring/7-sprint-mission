package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CreateChannelResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        ChannelType type,
        String name,
        String description
) {
    public static CreateChannelResponseDto from(Channel channel) {
        return CreateChannelResponseDto.builder()
                .id(channel.getId())
                .createdAt(channel.getCreateAt())
                .updatedAt(channel.getUpdateAt())
                .type(channel.getChannelType())
                .name(channel.getChannelName())
                .description(channel.getDesc())
                .build();
    }
}