package com.sprint.mission.discodeit.common.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class ContextTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        SecurityContext context = SecurityContextHolder.getContext();
        return () -> {
            try {
                if (contextMap != null)
                    MDC.setContextMap(contextMap);
                SecurityContextHolder.setContext(context);
                runnable.run();
            } finally {
                MDC.clear();
                SecurityContextHolder.clearContext();
            }
        };
    }
}
