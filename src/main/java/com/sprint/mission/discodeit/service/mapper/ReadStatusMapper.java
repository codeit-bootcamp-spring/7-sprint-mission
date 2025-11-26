package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.entity.ReadStatusEntity;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

    ReadStatus toReadStatus(ReadStatusEntity readStatusEntity);
    ReadStatusEntity toReadStatusEntity(ReadStatus readStatus);
    ReadStatusDto toReadStatusDto(ReadStatusEntity readStatusEntity);
}
