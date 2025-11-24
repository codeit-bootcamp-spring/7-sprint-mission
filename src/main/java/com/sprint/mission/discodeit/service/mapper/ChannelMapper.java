package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.entity.ChannelEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

    Channel toChannel(ChannelEntity channelEntity);

    ChannelEntity toChannelEntity(Channel channel);
}
