package com.sprint.mission.discodeit.config;

import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        SecurityContext capturedContext = SecurityContextHolder.getContext();

        return () -> {
            try {
                SecurityContextHolder.setContext(capturedContext);
                runnable.run();
            } finally {
                SecurityContextHolder.clearContext();
            }
        };
    }
}
