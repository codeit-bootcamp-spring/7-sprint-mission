package com.sprint.mission.discodeit.global.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod handlerMethod) {
            log.info("{}.{}",
                    handlerMethod.getBeanType().getSimpleName(),
                    handlerMethod.getMethod().getName()
            );
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
