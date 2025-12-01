package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReadStatusMapper {
    public ReadStatusResponseDto toDto(ReadStatus readStatus) {
        if (readStatus == null) {
            throw new IllegalArgumentException("readStatus is null");
        }

        return new ReadStatusResponseDto(
                readStatus.getId(),
                readStatus.getUser().getId(),
                readStatus.getChannel().getId(),
                readStatus.getLastReadAt()
        );
    }

    public List<ReadStatusResponseDto> toDtoList(List<ReadStatus> readStatuses) {
        if (readStatuses == null || readStatuses.isEmpty()) {
            return List.of();
        }

        return readStatuses.stream()
                .map(readstatus -> toDto(readstatus))
                .toList();
    }

}
