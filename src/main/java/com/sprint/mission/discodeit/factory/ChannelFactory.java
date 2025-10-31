package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.request.ChannelCreateReq;
import com.sprint.mission.discodeit.entity.Channel;

public class ChannelFactory {
    private ChannelFactory(){}

    public static Channel create(ChannelCreateReq req){
        return  Channel.builder()
                .managerId(req.managerId())
                .name(req.name())
                .description(req.description())
                .build();
    }
}
