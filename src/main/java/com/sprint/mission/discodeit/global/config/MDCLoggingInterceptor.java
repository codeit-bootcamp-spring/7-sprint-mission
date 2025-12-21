package com.sprint.mission.discodeit.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID_KEY = "requestId";
    private static final String REQUEST_METHOD_KEY = "requestMethod";
    private static final String REQUEST_URL_KEY = "requestUrl";
    private static final String REQUEST_ID_HEADER = "Discodeit-Request-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 요청 ID 생성 (UUID)
        String requestId = UUID.randomUUID().toString();

        // MDC에 요청 정보 추가
        MDC.put(REQUEST_ID_KEY, requestId);
        MDC.put(REQUEST_METHOD_KEY, request.getMethod());
        MDC.put(REQUEST_URL_KEY, request.getRequestURI());

        // 응답 헤더에 요청 ID 추가
        response.setHeader(REQUEST_ID_HEADER, requestId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 요청 처리 완료 후 MDC 정리 (메모리 누수 방지)
        MDC.clear();
    }
}