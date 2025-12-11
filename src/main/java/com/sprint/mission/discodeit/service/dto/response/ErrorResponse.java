package com.sprint.mission.discodeit.service.dto.response;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

    private Instant timestamp;
    private String code;
    private String message;
    private Map<String, Object> details;
    private String exceptionType; // 발생한 예외의 클래스 이름
    private int status; // HTTP 상태코드
}
