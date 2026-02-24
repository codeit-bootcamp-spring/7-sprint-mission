package com.sprint.mission.discodeit.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public class DiscodeitException extends RuntimeException {

    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = Map.of(); // NOTE: 부가정보에 대한 정보 민감성, 이득이 없는 요청에 대해선 생략하기위해 사용
    }

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = details == null ? Map.of() : Map.copyOf(details); // 불변성 유지를 위해 copyOf 사용
    }

    public DiscodeitException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.details = details == null ? Map.of() : Map.copyOf(details);

    }

}

