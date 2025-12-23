package com.sprint.mission.discodeit.dto.mapper;

import com.sprint.mission.discodeit.dto.entity.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {
    public static ReadStatusDto toDto(ReadStatus readStatus) {
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.user.getId(),
                readStatus.channel.getId(),
                readStatus.getLastReadAt()
        );
    }
}
