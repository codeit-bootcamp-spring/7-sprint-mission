package com.sprint.mission.discodeit.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ContextCopyingTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> callerMdc = MDC.getCopyOfContextMap();
        SecurityContext callerSecurityContext = SecurityContextHolder.getContext();

        return () -> {

            try {
                if (callerMdc != null) {
                    MDC.setContextMap(callerMdc);
                } else {
                    MDC.clear();
                }

                SecurityContext newContext = SecurityContextHolder.createEmptyContext();
                newContext.setAuthentication(callerSecurityContext.getAuthentication());
                SecurityContextHolder.setContext(newContext);

                runnable.run();
            } finally {
                MDC.clear();
                SecurityContextHolder.clearContext();
            }
        };
    }
}
