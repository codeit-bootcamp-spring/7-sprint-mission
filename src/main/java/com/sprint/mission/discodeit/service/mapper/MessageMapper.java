package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "channel.id", target = "channelId")
    MessageDto toDto(Message message);
}
