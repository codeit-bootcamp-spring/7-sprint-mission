package com.sprint.mission.discodeit.dto.request.userStatus;

import com.sprint.mission.discodeit.entityElement.UserStatusElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserStatusUpdateRequestDto<T> {

    private UUID userStatusId;
    private UserStatusElement type;
    private T updateValue;
}
