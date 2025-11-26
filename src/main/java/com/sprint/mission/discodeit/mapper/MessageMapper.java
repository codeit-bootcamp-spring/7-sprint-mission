package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

  MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

  @Mapping(target = "channelId", source = "channel.id")
  MessageResponseDto toResponseDto(Message message);

}
