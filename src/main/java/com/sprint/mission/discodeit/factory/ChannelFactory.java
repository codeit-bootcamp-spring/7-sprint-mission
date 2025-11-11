package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public class ChannelFactory {
    private ChannelFactory(){}

    public static Channel create(UUID managerId, ChannelCreateReq req){
        return Channel.createPublic(
                managerId,
                req.name(),
                req.description()
        );
    }

    public static Channel create(UUID managerId,ChannelCreateSecReq req){
        return Channel.createPrivate(
                managerId,
                req.userIds()
        );
    }
}
