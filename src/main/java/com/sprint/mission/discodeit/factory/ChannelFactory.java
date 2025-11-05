package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.entity.Channel;

public class ChannelFactory {
    private ChannelFactory(){}

    public static Channel create(ChannelCreateReq req){
        return Channel.createPublic(
                req.managerId(),
                req.name(),
                req.description()
        );
    }

    public static Channel create(ChannelCreateSecReq req){
        return Channel.createPrivate(
                req.managerId(),
                req.userIds()
        );
    }
}
