package com.sprint.mission.discodeit.global.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class MDCLoggingInterceptor implements HandlerInterceptor {

    public static final String REQUEST_ID = "requestId";
    public static final String METHOD = "method";
    public static final String URI = "uri";

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String requestId = UUID.randomUUID().toString();

        MDC.put(REQUEST_ID, requestId);
        MDC.put(METHOD, request.getMethod());
        MDC.put(URI, request.getRequestURI());

        response.setHeader("Discodeit-Request-ID", requestId);

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        MDC.clear(); //스레드 재사용을 위해 삭제
    }
}
