package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.dto.response.ChannelDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    ChannelDto toDto(Channel channel);
}
