package com.sprint.mission.discodeit.common.exceptions;

import com.sprint.mission.discodeit.common.enums.ErrorCode;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
public class DiscodeitException extends RuntimeException{
    private final Instant timestamp = Instant.now();
    private final ErrorCode errorCode;
    protected final Map<String, Object> details;

    public DiscodeitException(Class<?> entity, ErrorCode errorCode, Map<String, Object> details) {
        this.errorCode = errorCode;
        this.details = details;
        this.details.put("entity", entity.getSimpleName());
        this.details.put("description", errorCode.getDescription());
    }

    public DiscodeitException(Class<?> entity, ErrorCode errorCode) {
        this(entity, errorCode, new HashMap<>());
    }


}
