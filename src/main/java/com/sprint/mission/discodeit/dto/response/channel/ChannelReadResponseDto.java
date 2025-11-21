package com.sprint.mission.discodeit.dto.response.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entityElement.ChannelType;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChannelReadResponseDto {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String description;
    private ChannelType type;

    private HashSet<UUID> participantIds ;

    public static ChannelReadResponseDto from(Channel channel) {
        return ChannelReadResponseDto.builder()
                .name(channel.getName())
                .description(channel.getDescription())
                .type(channel.getType())
                .id(channel.getId())
                .createdAt(channel.getCreatedAt())
                .updatedAt(channel.getUpdatedAt())
                .participantIds(new HashSet<>())
                .build();
    }

}
