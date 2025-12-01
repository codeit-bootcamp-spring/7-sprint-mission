package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

    ReadStatusDto toDto(ReadStatus entity);


}