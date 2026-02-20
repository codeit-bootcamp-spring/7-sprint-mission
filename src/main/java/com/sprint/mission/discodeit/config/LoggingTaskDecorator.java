package com.sprint.mission.discodeit.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

@Slf4j
public class LoggingTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String,String> mdcContext = MDC.getCopyOfContextMap();

        return ()->{
            try{
                if(mdcContext!= null){
                    MDC.setContextMap(mdcContext);
                    log.info("MDC context 전파");
                }
                runnable.run();
            }
            finally {
                MDC.clear();
            }
        };
    }
}
