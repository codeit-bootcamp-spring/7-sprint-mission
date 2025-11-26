package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

    BinaryContent toBinaryContent(BinaryContentEntity binaryContentEntity);
    BinaryContentEntity toBinaryContentEntity(BinaryContent binaryContent);

}
