package com.sprint.mission.discodeit.config;


import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityContextDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        SecurityContext context = SecurityContextHolder.getContext();

        log.info("security context 전파 정보 캡쳐");
        return ()->{
            try{
                SecurityContextHolder.setContext(context);
                log.info("security context 전파 완료");
                 runnable.run();
            }
            finally {
                SecurityContextHolder.clearContext();
            }
        };
    }
}
