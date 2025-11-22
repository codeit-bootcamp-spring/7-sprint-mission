package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

    public BinaryContentDto toDto(BinaryContent binaryContent) {
        return binaryContent != null ?
                BinaryContentDto.from(binaryContent) : null;
    }
}

