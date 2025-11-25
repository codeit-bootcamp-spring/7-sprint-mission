package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.Channel;

import java.time.Instant;
import java.util.List;

public record ChannelDto(
        String id,
        String type,
        String name,
        String description,
        List<String> participantIds,
        Instant lastMessageAt
) {
    public static ChannelDto from(Channel channel){
        return new ChannelDto(
                channel.getId(),
                channel.getType().toString(),
                channel.getName(),
                channel.getDescription(),
                channel.getMembers(),
                null);
    }
}
