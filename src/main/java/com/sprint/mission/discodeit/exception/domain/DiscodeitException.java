package com.sprint.mission.discodeit.exception.domain;

import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class DiscodeitException extends RuntimeException{

    final Instant timestamp = Instant.now();
    final ErrorCode errorCode ;
    final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details){
        this.errorCode = errorCode;
        this.details = details;
    }
}
