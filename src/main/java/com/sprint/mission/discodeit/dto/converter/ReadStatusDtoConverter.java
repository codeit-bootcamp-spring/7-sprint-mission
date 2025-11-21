package com.sprint.mission.discodeit.dto.converter;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReadStatusDtoConverter {

    public ReadStatusResponseDto toResponseDto(ReadStatus readStatus) {
        return new ReadStatusResponseDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt()
        );
    }
}
