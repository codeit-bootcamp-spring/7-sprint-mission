package com.sprint.mission.discodeit.dto.request.message;

import com.sprint.mission.discodeit.entityElement.MessageElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class MessageUpdateRequestDto<T> {
    private UUID messageId;
    private MessageElement type;
    private T updateValue;
}
