package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  BinaryContentMapper INSTANCE = Mappers.getMapper(BinaryContentMapper.class);

  BinaryContentResponseDto toResponseDto(BinaryContent binaryContent);
}
