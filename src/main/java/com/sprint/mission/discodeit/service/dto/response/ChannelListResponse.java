package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.Channel;

import java.util.List;
import java.util.UUID;

public record ChannelListResponse(
        UUID id,
        String type,
        List<UUID> participantIds
) {
    public static ChannelListResponse from(Channel channel){
        return new ChannelListResponse(channel.getId(),
                channel.getType().toString(),
                channel.getMembers());
    }
}
