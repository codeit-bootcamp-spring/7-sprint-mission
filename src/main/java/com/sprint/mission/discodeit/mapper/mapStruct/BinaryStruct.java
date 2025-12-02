package com.sprint.mission.discodeit.mapper.mapStruct;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BinaryStruct {

    BinaryStruct INSTANCE = Mappers.getMapper(BinaryStruct.class);

    BinaryContentDto toDto(BinaryContent binaryContent);
}
