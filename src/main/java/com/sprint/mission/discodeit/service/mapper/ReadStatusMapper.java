package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.entity.ReadStatusEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

    ReadStatus toReadStatus(ReadStatusEntity readStatusEntity);
    ReadStatusEntity toReadStatusEntity(ReadStatus readStatus);
}
