package com.sprint.mission.discodeit.common.exceptions.readStatus;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {
    public ReadStatusNotFoundException(UUID id) {
        super(id, ErrorCode.NOT_FOUND);
    }


}
