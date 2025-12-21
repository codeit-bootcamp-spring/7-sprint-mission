package com.sprint.mission.discodeit.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
    private static final String START_TIME_ATTR = "REQ_START_TIME";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
        // 로그가 많으면 DEBUG로 내리기!
        log.info("HTTP {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }
    /*
     필요하면 postHandle 구현
     */

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Object startObj = request.getAttribute(START_TIME_ATTR);
        long timeMs = startObj instanceof Long ? (System.currentTimeMillis() - (Long) startObj) : -1;

        if (ex != null) {
            log.warn("HTTP {} {} -> {} ({}ms) exception = {}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), timeMs, ex.getClass().getSimpleName());
        } else {
            log.info("HTTP {} {} -> {} ({}ms)",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), timeMs);
        }
    }
}
