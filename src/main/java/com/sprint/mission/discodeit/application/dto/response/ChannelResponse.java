package com.sprint.mission.discodeit.application.dto.response;

import com.sprint.mission.discodeit.domain.Channel;
import lombok.Getter;

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
