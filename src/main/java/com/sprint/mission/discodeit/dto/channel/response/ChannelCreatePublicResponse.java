package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelCreatePublicResponse(
        UUID bose
        , UUID chennalID
        , ChannelType channelType
        , String channelName
) {
    public static  ChannelCreatePublicResponse from(Channel channel){
        return new ChannelCreatePublicResponse(
                channel.getBose()
                ,channel.getId()
                ,channel.getType()
                ,channel.getChannelName()
        );
    }

}
