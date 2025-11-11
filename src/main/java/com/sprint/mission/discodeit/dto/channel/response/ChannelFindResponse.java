package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponse(
        UUID bose,
        String channelName,
        ChannelType channelType,
        List<UUID> users,
        String description,
        Instant lastMessageTime
) {
    public static ChannelFindResponse from(Channel channel,Instant lastMessageTime){
        return new ChannelFindResponse(
                channel.getId(),
                channel.getName(),
                channel.getType(),
                //프라이빗 아니면 일단 널
                null,
                channel.getDescription(),
                lastMessageTime

        );
    }
    //private용
    public ChannelFindResponse isPrivate(Channel channel) {
        return new ChannelFindResponse(
                this.bose(),
                this.channelName(),
                this.channelType(),
                null,
                this.description(),
                this.lastMessageTime()
        );
    }

}
