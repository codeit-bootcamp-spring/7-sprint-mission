package com.sprint.mission.discodeit.exception.domain.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.domain.DiscodeitException;

import java.util.Map;

public class ReadStatusException extends DiscodeitException {
    public ReadStatusException(ErrorCode errorCode, Map<String,Object> details){
        super(errorCode,details);
    }
}
