package com.sprint.mission.discodeit.dto.request.readStatus;

import com.sprint.mission.discodeit.entityElement.ReadStatusElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
@Getter
@AllArgsConstructor
public class ReadStatusUpdateRequestDto<T> {
    private ReadStatusElement type;
    private UUID readStatusId;
    private T updateValue;

    public T getSafeUpdateValue(){
        if(type != ReadStatusElement.READ_LAST_TIME) return updateValue;
        return (T) Instant.parse(updateValue.toString());
    }
}
