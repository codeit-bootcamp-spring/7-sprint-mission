package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Res_Channel( //all private final
        @NotBlank(message = "channelName is mandatory")
        UUID channelId,
        Instant createdAt,
        Instant updatedAt,

        String channelName,
        ChannelType channelType,
        String description
) {
    public static Res_Channel from(Channel channel) {
        return Res_Channel.builder()
                .channelId(channel.getId())
                .createdAt(channel.getCreatedAt())
                .updatedAt(channel.getUpdatedAt())
                .channelName(channel.getChannelName())
                .channelType(channel.getChannelType())
                .description(channel.getDescription())
                .build();
    }
}
