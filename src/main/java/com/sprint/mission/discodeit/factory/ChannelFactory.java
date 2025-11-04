package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public class ChannelFactory {
    private ChannelFactory(){}

    public static Channel create(ChannelCreateReq req){
        return  Channel.builder()
                .managerId(req.managerId())
                .name(req.name())
                .description(req.description())
                .publicType(ChannelType.PUBLIC)
                .build();
    }

    public static Channel create(ChannelCreateSecReq req){
        return  Channel.builder()
                .name("비밀방"+ UUID.randomUUID())
                .managerId(req.managerId())
                .users(req.users())
                .publicType(ChannelType.PRIVATE)
                .build();

    }
}
