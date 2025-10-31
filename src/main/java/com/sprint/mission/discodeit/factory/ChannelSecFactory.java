package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.entity.Channel;

public class ChannelSecFactory {
    private ChannelSecFactory(){}

    public static Channel create(ChannelCreateSecReq req){
        return  Channel.builder()
                .managerId(req.managerId())
                .users(req.users())
                .build();

    }
}
