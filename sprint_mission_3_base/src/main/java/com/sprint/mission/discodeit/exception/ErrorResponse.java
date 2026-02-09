package com.sprint.mission.discodeit.exception;

import java.util.Map;

public record ErrorResponse(
        String timestamp,
        String code,
        String message,
        Map<String, Object> details,
        String exceptionType,
        int status
) {}
