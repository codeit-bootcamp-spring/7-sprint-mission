package com.sprint.mission.discodeit.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@Slf4j
public class ContextCopyingTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        SecurityContext securityContext = SecurityContextHolder.getContext();

        log.debug("컨텍스트 캡처 - MDC: {}, Security: {}", mdcContext, securityContext);

        return new DelegatingSecurityContextRunnable(() -> {
            try {
                if (mdcContext != null) MDC.setContextMap(mdcContext);

                log.debug("컨텍스트 전파 완료 - Thread: {}, Security: {}, TraceId: {}",
                        Thread.currentThread().getName(),
                        securityContext,
                        MDC.get("traceId"));

                runnable.run();

            } finally {
                MDC.clear();
                log.debug("컨텍스트 정리 완료");
            }
        });
    }
}
