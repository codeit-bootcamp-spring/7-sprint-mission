package com.sprint.mission.discodeit.entity.dto.channelDto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelDto(UUID id,
                         ChannelType type,    // public, private
                         String name,
                         String description,
                         List<UUID> participantIds,   // Private 전용
                         Instant lastMessageAt // 마지막 메시지 시간
) {

    public static ChannelDto from(Channel channel, List<UUID> participantIds, Message lastMessage) {

        Instant last = lastMessage != null ? lastMessage.getCreatedAt() : null;

        return ChannelDto.builder()
                .id(channel.getId())
                .name(channel.getName())
                .type(channel.getType())
                .participantIds(participantIds)
                .lastMessageAt(last)
                .build();
    }
}
