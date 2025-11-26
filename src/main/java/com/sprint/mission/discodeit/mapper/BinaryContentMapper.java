package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import org.springframework.stereotype.Component;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

    BinaryContentDto toDto(BinaryContent entity);


}