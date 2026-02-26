package com.sprint.mission.discodeit.global.async.decorator;

import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {

        // getContext()는 같은 객체의 참조를 반환하기 때문에, 메인 스레드에서 요청이 끝나고,
        // clear context가 요청되면, 비동기 스레드에서 해당 객체를 읽을 때 null이 참조될 수 있으므로,
        // 깊은 복사를 하여 context 값을 저장
        SecurityContext original = SecurityContextHolder.getContext();
        SecurityContext contextCopy = SecurityContextHolder.createEmptyContext();
        contextCopy.setAuthentication(original.getAuthentication());

        return () -> {
            try {
                SecurityContextHolder.setContext(contextCopy);
                runnable.run();
            } finally {
                SecurityContextHolder.clearContext();
            }
        };
    }
}