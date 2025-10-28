package com.sprint.mission.discodeit.dto.request.user;

import com.sprint.mission.discodeit.entityElement.UserElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto<T> {
    private UUID userId;
    private UserElement type;
    private T updateValue;
}
