package com.sprint.mission.discodeit.dto.request.readStatus;

import com.sprint.mission.discodeit.entityElement.ReadStatusElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
@Getter
@AllArgsConstructor
public class ReadStatusUpdateRequestDto<T> {
    private ReadStatusElement type;
    private UUID readStatusId;
    private T updateValue;
}
