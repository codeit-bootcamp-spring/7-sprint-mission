package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

    @Mapping(source = "fileType", target = "contentType")
    @Mapping(source = "fileSize", target = "size")
    BinaryContentDto toDto(BinaryContent binaryContent);
}
