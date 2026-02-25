package com.sprint.mission.discodeit.global.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;

import java.util.Map;

public class ContextCopyingTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(final Runnable runnable) {
        // 부모 스레드의 MDC를 복사 (final로 불변성 보장)
        final Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return new DelegatingSecurityContextRunnable(() -> {
            try {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                } else {
                    MDC.clear(); // 부모가 비어있다면 자식도 비워줌
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        });
    }
}
