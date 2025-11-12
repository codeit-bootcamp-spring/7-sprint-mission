package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.dto.user.response.BinaryResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelCreatePrivateResponse(
        UUID bose,
        UUID chennalID,
        ChannelType channelType,
        String channelName

) {
    public static  ChannelCreatePrivateResponse from(Channel channel){
        return new ChannelCreatePrivateResponse(
                channel.getBose(),
                channel.getId(),
                channel.getType(),
                channel.getChannelName()

        );
    }

}
