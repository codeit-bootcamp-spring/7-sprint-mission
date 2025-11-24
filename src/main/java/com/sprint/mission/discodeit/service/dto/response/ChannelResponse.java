package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.Channel;

import java.time.Instant;
import java.util.List;

public record ChannelResponse(
        String id,
        String type,
        String name,
        String description,
        List<String> participantIds,
        Instant lastMessageAt
) {
    public static ChannelResponse from(Channel channel){
        return new ChannelResponse(channel.getId(),
                channel.getType().toString(),
                channel.getMembers());
    }
}
