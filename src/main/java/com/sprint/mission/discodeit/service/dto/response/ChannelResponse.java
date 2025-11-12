package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.UUID;


public record ChannelResponse(
        UUID id,
        Instant createAt,
        Instant updateAt,
        String name

) {
    public static ChannelResponse from(Channel channel){
        return new ChannelResponse(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getChannelName());
    }
}
